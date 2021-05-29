package client;

import client.gui.forms.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Utilities;
import common.security.Security;
import common.Constants;
import common.models.*;
import common.models.TradeCurrentModel;
import common.transport.*;
import server.Server;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;

/**
 * The idea is to have a main controlling form/JPanel. This form acts as that form it is the  Controller, meaning
 * that all final actions are performed in it.
 */

public class MainFrame extends JFrame {
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson json ObjectMapper

    // Networking variables
    private String name = "Client";
    private String serverHost = "localhost";
    private int serverPort = 9000;
    private static JFrame fme = null;
    private ClientLogic objClientLogic = null;
    private String client_name = null;

    private TablePanel tablePanel;
    private TradePanel tradePanel;

    private String originalOrganisationLabelText;
    private String originalUsernameLabelText;
    private int user_id;
    private String username;
    private String password;
    private int asset_id;
    private String asset_name;
    private int org_unit_id;
    private String org_unit_name;
    private int account_type_id;
    private String account_type;

    Timer scheduler = null; // Swing Timer (scheduler) will run on the Event Dispatching Thread (EDT)
    private boolean useScheduler = false;
    private int initialDelay = 10;
    private int period = 15;

    private ArrayList<TradeCurrentModel> currentTrades;
    private ArrayList<AssetModel> assets = new ArrayList<>();

    public MainFrame(String title) {
        super(title);

        try {
            // Get host and port information from configuration file network.prop
            this.serverHost = Utilities.getProperty(MainFrame.class, "network.prop", "host");
            this.serverPort = Integer.parseInt(Utilities.getProperty(MainFrame.class, "network.prop", "port"));
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this, "Could not get connection properties",
                    "Property Retrieval message", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.name = name;
        this.fme = MainFrame.this; // assign owner

        // Gateway to the client's logic/transport message & return messages
        this.objClientLogic = new ClientLogic(this.name, this.serverHost, this.serverPort);

        // Design GUI
        JMenuBar menuBar = createMenuBar(); // create menubar and add to JFrame
        this.setJMenuBar(menuBar);

        tablePanel = new TablePanel();
        tradePanel = new TradePanel();
        tradePanel.enableTradingPanel(false);

        this.getContentPane().add(tradePanel, BorderLayout.NORTH);
        this.getContentPane().add(tablePanel, BorderLayout.CENTER);

        setDefaultLookAndFeelDecorated(true);
        setSize(800,450);
        setMinimumSize(this.getSize());
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo(null); // position in screen centre
        setVisible(true);

        // Window listeners
        // Needed to show Login dialog at start-up
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                setSchedulerProperties(); // setup 'Swing Timer' properties
                menuBar.getMenu(0).getItem(0).doClick();
            }
        });
        // Tidy-up is user hit Window close X or menu item 'Close'
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // User may or may not have logged in
                if (scheduler.isRunning()) {
                    scheduler.stop();
                }
                objClientLogic.GetOut();
            }
        });

        // ------- Custom Listeners -------

        // Use an simple Observer patten to communicate with other panels in the MainFrame - makes the code more easier
        // to follow.
        // Status Panel listener - note it is contained within the 'tablePanel' need a public method to easily get it.
        StatusPanel p = (StatusPanel) tablePanel.getStatusPanel();
        p.setStatusPanelListener(new IStatusPanelListener() {
            @Override
            public void onRefreshButtonClick() { // Refresh button clicked
                String s = tablePanel.getTable().getModel().getClass().getName(); // TODO: debug code
                if (tablePanel.getTable().getModel().getClass().getName().endsWith("TradeTableModel")) {
                    if (! refreshCurrentTrades()) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Error in returning current trades.",
                                "Refresh Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Sign-in to view current trades.",
                            "Refresh Message", JOptionPane.WARNING_MESSAGE);
                }
            }
            @Override
            public void onFilterSelect(boolean enablefilter) {
                RowSorter rowSorter = createRowFilter(tablePanel.getTable());
                filterByOrganisation((TableRowSorter) rowSorter, enablefilter);
            }
        });

        // Trade Panel Listener - it is directly within the MainFrame so no method needed to access it (private variable)
        tradePanel.setTradePanelListener(new ITradePanelListener() {
            @Override // Submit button clicked - saving a new Trade
            public void onSubmitButtonClick(String trade_type, int asset_id, String asset_name, int quantity, int price) { // data from trade panel
                // Send to server to create a trade
                Timestamp ts = (new Timestamp(new java.util.Date().getTime()));
                TradeCurrentModel trade = new TradeCurrentModel(0, trade_type, org_unit_id,
                        org_unit_name, user_id, username, asset_id, asset_name, quantity, price, ts);

                String jsonResult = objClientLogic.addTrade(trade); // add to database
                JSONAddTradeAcknowledgement objAddTradeAcknowledgement = null;
                try {
                    objAddTradeAcknowledgement = objectMapper.readValue(jsonResult,  JSONAddTradeAcknowledgement.class);
                } catch (JsonProcessingException jsonProcessingException) {
                    JOptionPane.showMessageDialog(fme,
                            "Add new trade parsing error!\nContact System Administrator. ",
                            "Add Trade Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (objAddTradeAcknowledgement.getSuccess().equals("1")) {
                    trade.setTrade_id(objAddTradeAcknowledgement.getTradeId()); // get new trade_id sent from server
                    ((TradeTableModel) tablePanel.getTable().getModel()).addTrade(trade); // add to table model
                    //TODO: debug code
                    String s = ts.toString();
                    System.out.println("onSubmitButton - MainFrame: " + trade_type + ", " + org_unit_id + ", " + org_unit_name +
                            ", " + user_id + ", " + username + ", " + asset_id + ", " + asset_name +
                            ", " + quantity + ", " + price + ", " + s);
                }
                else {
                    JOptionPane.showMessageDialog(fme,
                            objAddTradeAcknowledgement.getErrorMessage(),
                            "Add Trade Message", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override // asset selected from combobox
            public void onAssetSelected(int asset_id, String asset_name) {
                MainFrame.this.asset_id = asset_id;
                MainFrame.this.asset_name = asset_name;
                System.out.println(asset_id + " : " + asset_name);
            }
        });

        // Table Listener - it is directly within the MainFrame so no method needed to access it (private variable)
        tablePanel.setTableListener(new ITableListener() {
            public void rowDeleted(Constants.TABLE_TYPE table_type, int index, int id)  {

                // Used to delete a current trade - Popup menu in tablePanel
                System.out.println("rowDeleted - TableType: " + table_type + ", Row Position: " + index + ", TradeId: " + id); // TODO: debug code
                System.out.println("mainframe: rows in currentTrades: " + currentTrades.size()); // TODO: debug code

                // Remove from the server
                objClientLogic.deleteTrade(id); // remove it from the database // TODO: may need to check if successful ?
                ((TradeTableModel) tablePanel.getTable().getModel()).removeTrade(id); // remove from table model
            }

            @Override
            public void rowSelected(Constants.TABLE_TYPE table_type, int index, int id)  {
                if (table_type == Constants.TABLE_TYPE.empty) { return; }
                System.out.println("rowSelected - TableType: " + table_type + ", Row Position: " + index + ", TradeId: " + id); // TODO: debug code
            }
        });

        ActionListener taskExecutor = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Perform scheduled task - refresh current trades from server...
                // These action should be as quick as possible so not to hold-up the swing 'event dispatch thread'
                refreshCurrentTrades();
                System.out.println("timer - refresh current trades"); // TODO: debug code
                String latestMessage = objClientLogic.getLatestTradeMessage("Software Access Management"); // TODO: remove - test only
                //String latestMessage = objClientLogic.getLatestTradeMessage(org_unit_name); // TODO: add for final release
                displayTradeProcessedMessage(latestMessage);
            }
        };
        scheduler = new Timer(initialDelay, taskExecutor);
        scheduler.setRepeats(true);

    } // end constructor


    /**
     *  Updates the message panel's latest successful trade for your organisation
     * @param latestMessage latest message to be displayed
     */
    private void displayTradeProcessedMessage(String latestMessage) {
        ((JLabel)tablePanel.getMessagePanel().getComponent(0)).setText(latestMessage);
    }

    /**
     * Used to refresh current trade information to the table's Model
     * @return boolean success or failure
     */
    private boolean refreshCurrentTrades() {

        try {
            ArrayList<TradeCurrentModel> refreshedTrades = objClientLogic.getAllCurrentTrades();
            ((TradeTableModel) tablePanel.getTable().getModel()).refreshTrades(refreshedTrades);
        } catch (ClassCastException | IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Used to clear assets in TradePanel Assets combobox and refill the ArrayList that it displays
     * @return boolean - success or failure to complete
     */
    private boolean refreshAssets() {
        // Called to clear assets in TradePanel combobox and refill the ArrayList that it displays
        try {
            ArrayList<AssetModel> assets = new ArrayList<>();
            assets = objClientLogic.getAllAssets();
            this.assets.clear(); // safe to clear private MainFrame array of assets, no exception ?
            this.assets.addAll(assets); // reload private MainFrame array of assets...
            tradePanel.setAssetModels(this.assets); // reload combobox dropdown list of assets
        }
        catch(IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Used to refresh the Current Trades form the server
     * Set timer (scheduler) properties. Initial Delay and Period
     * @return boolean - success or failure to complete
     */
    private boolean setSchedulerProperties() {
        try {
            this.useScheduler = Boolean.parseBoolean(Utilities.getProperty(Server.class,"client.prop", "useScheduler"));
            this.initialDelay = Integer.parseInt(Utilities.getProperty(Server.class,"client.prop", "delay"));
            this.period = Integer.parseInt(Utilities.getProperty(Server.class,"client.prop", "period"));
        } catch (IOException e) {
            // Note: these variable are already initialised to reasonable default values - scheduler will be inactive
            return false;
        }
        return true;
    }

    /**
     * Uses a Json array with only one element that contains the user's details - more efficient to
     * do this in the Login process. Save a separate call to the server.
     * @param arrDetails Users details
     * @param clientName Name of this Client
     * @param password hashed password to be stored for future checks
     */
    public void setUserDetails(JSONUserDetail[] arrDetails, String clientName, String password) {
        user_id = arrDetails[0].getUser_id();
        username = arrDetails[0].getUsername();
        this.password = password;
        account_type = arrDetails[0].getAccount_type();
        account_type_id = arrDetails[0].getAccount_type_id();
        org_unit_id = arrDetails[0].getOrg_unit_id();
        org_unit_name = arrDetails[0].getOrg_unit_name();
        client_name = clientName;
        fme.setTitle("Client" + " - logged in as " + client_name); // TODO: debug code
    }

    /**
     * Used to manage all the local variables, menu items and other components. Dependent on Login or Logout.
     *
     * @param login boolean is login action
     */
    public void setLogonAndLogOffVariables(boolean login) {

        if (login) {
            tradePanel.enableTradingPanel(true);

            // Set organisation name and username in StatusPanel
            JPanel pnl = (JPanel) tablePanel.getStatusPanel();
            JLabel lbl0 = (JLabel) pnl.getComponent(0);
            lbl0.setText((originalUsernameLabelText = lbl0.getText()) + username);
            JLabel lbl1 = (JLabel) pnl.getComponent(1);
            lbl1.setText((originalOrganisationLabelText = lbl1.getText()) + org_unit_name);
            JCheckBox chk = (JCheckBox) pnl.getComponent(2);
            chk.setEnabled(true);

            // Ok - now set-up the menu items
            manageMenuItemsExtended(true); // menu items
        }
        else { // logoff option
            tradePanel.enableTradingPanel(false);

            // Clear other labels and fields
            displayTradeProcessedMessage("Display area the latest trade for your organisation!");
            getRootPane().getJMenuBar().getMenu(2).setEnabled(false);
            tradePanel.clearTradeTypeCombobox();
            tradePanel.clearAssetCombobox();
            JPanel pnl = tablePanel.getStatusPanel();
            JLabel lbl0 = (JLabel) pnl.getComponent(0);
            lbl0.setText(originalUsernameLabelText);
            JLabel lbl1 = (JLabel) pnl.getComponent(1);
            lbl1.setText(originalOrganisationLabelText);
            JCheckBox chk = (JCheckBox) pnl.getComponent(2);
            chk.setEnabled(false);

            // Clear local variables for this user's parameters
            user_id = 0;
            username = null;
            password = null;
            org_unit_id = 0;
            org_unit_name = null;
            account_type_id = 0;
            fme.setTitle("Client" + " - logged in as - logged out."); // TODO: debug code

            // Manage / reset menu items to before LogIn status
            manageMenuItemsExtended(false); // menu items
        }
    }

    /**
     *  Used to set-up table sorting and filtering
     * @param table table to add the sorter / filer to
     * @return TableRowSorter
     */
    public TableRowSorter createRowFilter(JTable table) {
        // Based on information/code from https://www.logicbig.com/tutorials/java-swing/jtable-row-filter.html
        RowSorter<? extends TableModel> rs = table.getRowSorter();
        if (rs == null) {
            table.setAutoCreateRowSorter(true);
            rs = table.getRowSorter();
        }
        //TableRowSorter<? extends TableModel> rowSorter = (TableRowSorter<? extends TableModel>) rs;

        TableRowSorter<? extends TableModel> rowSorter = (rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;
        if (rowSorter == null) {
            throw new RuntimeException("Cannot find rowSorter: " + rs);
        }
        return rowSorter;
    }

    /**
     * Used to filter the table on 'Organisation' column. Only filter/show the logged in user's organisation
     *
     * @param rowSorter TableRowSorter
     * @param enable boolean - filter enabled (on) or disabled (off)
     */
    private void filterByOrganisation(TableRowSorter rowSorter, boolean enable) {
        // Used by checkbox to turn on or off filter on the user's Log-on set organisation
        if (enable) {
            System.out.println(org_unit_name); // TODO: debug code
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + org_unit_name));
        }
        else {
            rowSorter.setRowFilter(null);
        }
    }

    /**
     * // Manage all menu items based around the logged-in action. Enables critical menu items.
     *
     * @param isLoggingIn boolean logged-in or not logged in
     */
    private void manageMenuItemsExtended(boolean isLoggingIn) {
        // Manage all menu items based around logging in action
        if (isLoggingIn) {
            manageMenuItems();
            this.getRootPane().getJMenuBar().getMenu(0).getItem(0).setEnabled(false); // login
            this.getRootPane().getJMenuBar().getMenu(0).getItem(1).setEnabled(true);  // logout
            this.getRootPane().getJMenuBar().getMenu(0).getItem(2).setEnabled(true);  // change pwd
            this.getRootPane().getJMenuBar().getMenu(0).getItem(3).setEnabled(true);  // refresh
            this.getRootPane().getJMenuBar().getMenu(1).getItem(0).setEnabled(true);  // view
        }
        else {
            this.getRootPane().getJMenuBar().getMenu(0).getItem(0).setEnabled(true);  // login
            this.getRootPane().getJMenuBar().getMenu(0).getItem(1).setEnabled(false); // logout
            this.getRootPane().getJMenuBar().getMenu(0).getItem(2).setEnabled(false); // change pwd
            this.getRootPane().getJMenuBar().getMenu(0).getItem(3).setEnabled(false); // refresh
            this.getRootPane().getJMenuBar().getMenu(1).getItem(0).setEnabled(false); // view
            this.getRootPane().getJMenuBar().getMenu(2).setEnabled(false); // tools
        }
    }

    /**
     * Used to manage 'View' main menu item at login - account type (admin or user) dependent
     */
    private void manageMenuItems() {
        if (this.account_type_id == 1) { // admin account
            this.getRootPane().getJMenuBar().getMenu(2).setEnabled(true); // tools
        }
        else if (this.account_type_id == 0) { //user account
            this.getRootPane().getJMenuBar().getMenu(2).setEnabled(false); // tools
        }
    }

    /**
     * Used to create the MainFrame's main menu and its listeners
     * @return Main menu bar
     */
    private JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // File Menu Item
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem miLogin = new JMenuItem("LogIn...");
        miLogin.setMnemonic(KeyEvent.VK_L);
        miLogin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));

        JMenuItem miLogout = new JMenuItem("LogOut");
        miLogout.setMnemonic(KeyEvent.VK_O);
        miLogout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        JMenuItem miChangePassword = new JMenuItem("Change Password...");
        miChangePassword.setMnemonic(KeyEvent.VK_C);
        miChangePassword.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        JMenuItem miRefreshAssets = new JMenuItem("Refresh Assets");
        miRefreshAssets.setMnemonic(KeyEvent.VK_R);
        miRefreshAssets.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

        JMenuItem miFileExit = new JMenuItem("Exit");
        miFileExit.setMnemonic(KeyEvent.VK_X);
        miFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        fileMenu.add(miLogin);
        fileMenu.add(miLogout);
        miLogout.setEnabled(false); // initial setting
        fileMenu.add(miChangePassword);
        miChangePassword.setEnabled(false); // initial setting
        fileMenu.add(miRefreshAssets);
        miRefreshAssets.setEnabled(false); // initial setting
        fileMenu.addSeparator();
        fileMenu.add(miFileExit);
        menuBar.add(fileMenu);

        miLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String buttonText = "Login";

                // Get from Login form fields
                Login dlgSetPassword = new Login(MainFrame.this, "Login",buttonText,true);
                Login.DialogResult dlgResult = dlgSetPassword.run(); // user form inputs

                if (dlgResult != null) {
                    if (dlgResult.getButtonName().equals(buttonText)) {

                        //String jsonResult = objClientLogic.ValidatePassword(dlgResult.getUsername(), String.valueOf(dlgResult.getPassword()));
                        String jsonResult = objClientLogic.ValidatePassword(new JSONLogin(dlgResult.getUsername(), String.valueOf(dlgResult.getPassword())));
                        if (! Utilities.isNullOrEmpty(jsonResult)) {
                            try {
                                JSONLoginResult objLoginResult = objectMapper.readValue(jsonResult, JSONLoginResult.class);
                                if (objLoginResult.getSuccess().equals("1")) {
                                    JSONUserDetail[] userDetail = objectMapper.readValue(objLoginResult.getJsonUserDetails(), JSONUserDetail[].class);
                                    setUserDetails(userDetail, objLoginResult.getClientName(), objLoginResult.getPassword());
                                    // Set the table's Model - use a new model to show Current Trades
                                    tablePanel.setTradeData(null);
                                    tablePanel.getTable().setAutoCreateRowSorter(true);
                                    // Now get all the current trades from the server
                                    objClientLogic.setUsername(dlgResult.getUsername());
                                    currentTrades = objClientLogic.getAllCurrentTrades();
                                    tablePanel.getTable().setModel(new TradeTableModel(currentTrades));
                                    tablePanel.setTableType();
                                    assets = objClientLogic.getAllAssets();
                                    tradePanel.setTradeTypes(new ArrayList<String>(List.of("BUY", "SELL")));
                                    tradePanel.setAssetModels(assets);
                                    tablePanel.setParameters(800, 4, 15, 6, 15, 4, 4, 12);
                                    setLogonAndLogOffVariables(true);
                                    JOptionPane.showMessageDialog(fme, objLoginResult.getMessage(),
                                            "Login Message", JOptionPane.INFORMATION_MESSAGE);
                                    if(useScheduler) {
                                        // Start up SwingTimer (scheduler) to refresh current trades.
                                        // Note: Will only start if login was successful.
                                        scheduler.setInitialDelay(initialDelay);
                                        scheduler.setDelay(period);
                                        scheduler.start();
                                    }
                                }
                                else { // invalid username or password
                                    objClientLogic.GetOut(); // close the connection
                                    JOptionPane.showMessageDialog(fme,
                                            objLoginResult.getErrorMessage(),
                                            "Login Message", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException jsonProcessingException) {
                                objClientLogic.GetOut();// close the connection
                                JOptionPane.showMessageDialog(fme,
                                        "Login parsing error!\nContact System Administrator. ",
                                        "Login Message", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(fme,
                                    "Check that the server is running!\nContact system administrator if error persists.",
                                    "Login Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else { /* must have clicked Cancel */ }
                }
            }
        });

        miLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close down client (gracefully?)
                int response = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure?",
                        "Logout Message", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    if (tablePanel.getTable().getModel().getClass().getName().endsWith("TradeTableModel")) {
                        // Shutdown timer
                        if (scheduler.isRunning()) {
                            scheduler.stop();
                        }
                        // User must be logged in, if 'EmptyTableModel' then not logged in. (check)
                        tablePanel.getTable().setModel(new EmptyTableModel());
                        tablePanel.getTable().setAutoCreateRowSorter(false);
                        //System.out.println(tablePanel.getTable().getModel().getClass().getName()); // TODO: debug code
                        setLogonAndLogOffVariables(false);
                        objClientLogic.GetOut(); // Close the connection
                    }
                }
            }
        });

        miChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String buttonText = "Change Password";

                PasswordChange dlgSetPassword = new PasswordChange(MainFrame.this, "Change Password",
                                                            MainFrame.this.username, buttonText,true);
                PasswordChange.DialogResult dlgResult = dlgSetPassword.run(); // user form inputs

                String jsonResult = null;
                if (dlgResult != null) {
                    if (dlgResult.getButtonName().equals(buttonText)) {
                        // Hash both passwords - make sure the hashed version of the entered password equals the stored version on this client
                        String oldHashedPwd = Security.GeneratePasswordHash(String.valueOf(dlgResult.getOldPassword()));
                        String newHashedPwd = null;
                        if (MainFrame.this.password.equals(oldHashedPwd)) {
                            newHashedPwd = Security.GeneratePasswordHash(String.valueOf(dlgResult.getNewPassword()));
                        }
                        else {
                            JOptionPane.showMessageDialog(MainFrame.this,
                                    "Old password value is not correct!\nNeed to supply current password.",
                                    "Change Password Message", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        // Now send off new hashed password to be stored in the database and update the hashed password stored on this client
                        jsonResult= objClientLogic.changePassword(new JSONChangePassword(MainFrame.this.username, MainFrame.this.password, newHashedPwd));
                        JSONAcknowledgement objAcknowledgement = null;
                        try {
                            objAcknowledgement = objectMapper.readValue(jsonResult,  JSONAcknowledgement.class);
                        } catch (JsonProcessingException jsonProcessingException) {
                            JOptionPane.showMessageDialog(fme,
                                    "Change Password parsing error!\nContact System Administrator. ",
                                    "Change Password Message", JOptionPane.ERROR_MESSAGE);
                        }
                        if (objAcknowledgement != null) {
                            if (objAcknowledgement .getSuccess().equals("1")) {
                                MainFrame.this.password = newHashedPwd;
                                JOptionPane.showMessageDialog(MainFrame.this,
                                        objAcknowledgement .getMessage(),
                                        "Change Password Message", JOptionPane.INFORMATION_MESSAGE);
                            }
                            if (objAcknowledgement .getSuccess().equals("0")) {
                                JOptionPane.showMessageDialog(MainFrame.this,
                                        objAcknowledgement .getErrorMessage(),
                                        "Change Password Message", JOptionPane.ERROR_MESSAGE);
                            }

                        } else { /* do nothing user must have hit 'Cancel' */ }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(MainFrame.this, "No information was returned!",
                            "Change Password Message", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        miRefreshAssets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (! refreshAssets()) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Assets were not refreshed!",
                            "Refresh Assets Message", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        miFileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Let user confirm exiting the application
                int response = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure?",
                        "Exit Message", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // The WindowClosing Event will/should handle the connection close
                    if (scheduler.isRunning()) {
                        scheduler.stop();
                    }
                    System.exit(0);
                }
            }
        });

        // View Menu Item
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        JMenuItem miHistory = new JMenuItem("History...");
        miHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));

        miHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get all assets
                    ArrayList<AssetModel> assets = objClientLogic.getAllAssets();
                    // Object 'objClientLogic' is passed so the chart dialog can get data for a selected Asset
                    Chart chart = new Chart(MainFrame.this, "Asset Trading Chart", true, objClientLogic, assets);
                    chart.setVisible(true); // Set visible here - model Dialog form
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "An error occurred in generating the chart.\nContact your system administrator!",
                            "Trade History Chart", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        viewMenu.add(miHistory);
        menuBar.add(viewMenu);
        miHistory.setEnabled(false); // initial setting

        // Tools Menu Item
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        // Tool Menu - Sub menus
        JMenu miUser;
        miUser = new JMenu("User");
        JMenuItem smiCreateUser;
        smiCreateUser = new JMenuItem("Create User...");
        miUser.add(smiCreateUser);
        JMenuItem smiEditUser;
        smiEditUser = new JMenuItem("Edit User...");
        miUser.add(smiEditUser);
        toolsMenu.add(miUser);

        JMenu miOrganisation;
        miOrganisation = new JMenu("Organisation");
        JMenuItem smiCreateOrganisation;
        smiCreateOrganisation = new JMenuItem("Create Organisation...");
        miOrganisation.add(smiCreateOrganisation);
        JMenuItem smiEditOrganisation;
        smiEditOrganisation = new JMenuItem("Edit Organisation...");
        miOrganisation.add(smiEditOrganisation);
        toolsMenu.add(miOrganisation);

        JMenuItem miAddAsset = new JMenuItem("Create Asset...");
        miAddAsset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.SHIFT_MASK));

        JMenuItem miAddOrganisation = new JMenuItem("Add Organisation...");
        miAddOrganisation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.SHIFT_MASK));

        JMenuItem miAddHolding = new JMenuItem("Asset Holdings...");
        miAddHolding.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.SHIFT_MASK));

        smiCreateUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String  buttonText = "Create";
                manageUser(fme, buttonText, "Create User");
            }
        });

        smiEditUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String  buttonText = "Update";
                manageUser(fme, buttonText, "Edit User");
            }
        });

        smiCreateOrganisation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String  buttonText = "Create";
                manageOrganisation(fme, buttonText, "Create Organisation Unit");

            }
        });

        smiEditOrganisation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String  buttonText = "Update";
                manageOrganisation(fme, buttonText, "Edit Organisation Unit");
            }
        });

        miAddAsset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String  buttonText = "Create";

                CreateAsset dlgCreateAsset = new CreateAsset(MainFrame.this, "Create Asset", buttonText, true);
                CreateAsset.DialogResult dlgResult = dlgCreateAsset.run(); // user form inputs

                if (dlgResult != null) {
                    if (dlgResult.getButtonName().equals(buttonText)) {
                        System.out.println("Asset Id: " + dlgResult.getAssetId() +
                                ", AssetName: " + dlgResult.getAssetName());
                        try {
                            String jsonResult = objClientLogic.addAsset(dlgResult.getAssetId(), dlgResult.getAssetName());
                            if (! Utilities.isNullOrEmpty(jsonResult)) {
                                JSONAcknowledgement objAcknowledgement = objectMapper.readValue(jsonResult, JSONAcknowledgement.class);
                                if (objAcknowledgement.getSuccess().equals("1")) {
                                    JOptionPane.showMessageDialog(fme, objAcknowledgement.getMessage(),
                                            "Create Asset Message", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(fme, objAcknowledgement.getErrorMessage(),
                                            "Create Asset Message", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(fme,
                                        "Check server is running!\nContact System Administrator. ",
                                        "Create Asset Message", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (JsonProcessingException jsonProcessingException) {
                            JOptionPane.showMessageDialog(fme,
                                    "Create asset parsing error!\nContact System Administrator. ",
                                    "Create Asset Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        miAddHolding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // These are be retrieved from the server
                ArrayList<OrgUnitModel> orgUnits = null;
                ArrayList<AssetModel> assets = null;
                ArrayList<AssetHoldingModel> holdings = null;

                try {
                    orgUnits = objClientLogic.getAllOrgUnits(); // Get Organisations
                    assets = objClientLogic.getAllAssets(); // Get Assets
                    holdings = objClientLogic.getAllAssetHoldings(); // Get Asset Holdings
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                final String buttonText = "Process";
                ManageHoldings dlgCreateAsset = new ManageHoldings(MainFrame.this, "Manage Holdings",
                                                                buttonText, true, orgUnits, assets, holdings);
                ManageHoldings.DialogResult dlgResult = dlgCreateAsset.run(); // user form inputs

                if (dlgResult != null) {
                    String messageDialogTitle = null;
                    try {
                        String jsonResult = null;
                        if (dlgResult.isCreate()) { // create
                            // Now create the holding
                            jsonResult = objClientLogic.processAssetHolding(dlgResult.getOrgUnitId(), dlgResult.getAssetId(), dlgResult.getQuantity(), "create");
                            messageDialogTitle = "Create Asset Holding";
                        } else { // update
                            jsonResult = objClientLogic.processAssetHolding(dlgResult.getOrgUnitId(), dlgResult.getAssetId(), dlgResult.getQuantity(), "update");
                            messageDialogTitle = "Create Asset Holding";
                        }
                        if (!Utilities.isNullOrEmpty(jsonResult)) {
                            JSONAcknowledgement objAcknowledgement = objectMapper.readValue(jsonResult, JSONAcknowledgement.class);
                            if (objAcknowledgement.getSuccess().equals("1")) {
                                JOptionPane.showMessageDialog(fme, objAcknowledgement.getMessage(),
                                        messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(fme, objAcknowledgement.getErrorMessage(),
                                        messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(fme,
                                    "Check server is running!\nContact System Administrator. ",
                                    messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (JsonProcessingException jsonProcessingException) {
                        JOptionPane.showMessageDialog(fme,
                                messageDialogTitle + " parsing error!\nContact System Administrator. ",
                                messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println("Change to holding: OrgUnitId: " + dlgResult.getOrgUnitId() +
                            ", OrgUnitName: " + dlgResult.getOrgUnitName() +
                            ", AssetId: " + dlgResult.getAssetId() +
                            ", AssetName: " + dlgResult.getAssetName() +
                            ", Quantity: " + dlgResult.getQuantity() +
                            ", IsCreate: " + dlgResult.isCreate());
                }
            }
        });

        toolsMenu.add(miAddAsset);
        toolsMenu.add(miAddHolding);
        menuBar.add(toolsMenu);
        toolsMenu.setEnabled(false); // initial setting

        // Help Menu Item
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem miHelpAbout = new JMenuItem("About...");
        helpMenu.add(miHelpAbout);
        menuBar.add(helpMenu);
        miHelpAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Version 1.0",
                        "Layout", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return menuBar;
    }

    private void manageOrganisation(JFrame parent, String buttonText, String dialogTitle) {
        // Used to show and mange both Add and Update an Organisation unit

        // These would be retrieved from the server
        ArrayList<OrgUnitModel> orgUnits = null;
        try {
            orgUnits = objClientLogic.getAllOrgUnits(); // Get Organisations
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (buttonText.equals("Update") && orgUnits.size() <= 0) {
            // Nothing to edit!
            JOptionPane.showMessageDialog(parent,
                    "There are no organisations to edit.",
                    "Edit Organisation Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Get from Organisation form fields
        ManageOrganisation dlgManageOrganisation = new ManageOrganisation(parent, dialogTitle, buttonText, true, orgUnits);
        ManageOrganisation.DialogResult dlgResult = dlgManageOrganisation.run(); // user form inputs

        if (dlgResult != null) {
            if (dlgResult.getButtonName().equals(buttonText)) {
                String messageDialogTitle = null;
                try {
                    String jsonResult = null;
                    if (buttonText.equals("Create")) {
                        jsonResult = objClientLogic.processOrgUnit(dlgResult.getOrgUnitId(),
                                dlgResult.getOrgUnitName(), dlgResult.getCredits(), "create");
                        messageDialogTitle = "Create Organisation Unit";
                    } else { // edit
                        jsonResult = objClientLogic.processOrgUnit(dlgResult.getOrgUnitId(),
                                dlgResult.getOrgUnitName(), dlgResult.getCredits(), "update");
                        messageDialogTitle = "Update Organisation Unit";
                    }
                    if (!Utilities.isNullOrEmpty(jsonResult)) {
                        JSONAcknowledgement objAcknowledgement = objectMapper.readValue(jsonResult, JSONAcknowledgement.class);
                        if (objAcknowledgement.getSuccess().equals("1")) {
                            JOptionPane.showMessageDialog(parent, objAcknowledgement.getMessage(),
                                    messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parent, objAcknowledgement.getErrorMessage(),
                                    messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(parent,
                                "Check server is running!\nContact System Administrator. ",
                                messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (JsonProcessingException jsonProcessingException) {
                    JOptionPane.showMessageDialog(parent,
                            messageDialogTitle + " parsing error!\nContact System Administrator. ",
                            messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Org Unit: " + dlgResult.getOrgUnitId() +
                        ", Organisation: " + dlgResult.getOrgUnitName() +
                        ", Credits: " + dlgResult.getCredits());
            }
        }
    }

    private void manageUser(JFrame parent, String buttonText, String dialogTitle) {
        // Used to show and mange both Add and Update a User

        // These would be retrieved from the server
        ArrayList<UserModel> users = null;
        ArrayList<OrgUnitModel> orgUnits = null;
        ArrayList<AccountTypeModel> accountTypes = null;
        try {
            if (! buttonText.equals("Create")) { users = objClientLogic.getAllUsers(); } // Get Users for editing
            orgUnits = objClientLogic.getAllOrgUnits(); // Get Organisations
            accountTypes = objClientLogic.getAllAccountTypes();// Get Account type
        } catch (IOException ioException) {
            ioException.printStackTrace(); // TODO: ?
        }

        if (buttonText.equals("Update") && users != null && users.size() <= 0) {
            // Nothing to edit!
            JOptionPane.showMessageDialog(parent,
                    "There are no users to edit.",
                    "Edit User Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String title = buttonText.equals("Create") ? "Create User" : "Edit User"; // dialog (title change)
        ManageUser dlgManageUser = new ManageUser(parent, title, buttonText, true, users, orgUnits, accountTypes);
        ManageUser.DialogResult dlgResult = dlgManageUser.run(); // user form inputs

        if (dlgResult != null) {
            if (dlgResult.getButtonName().equals(buttonText)) {
                String messageDialogTitle = null;
                try {
                    String jsonResult = null;
                    if (buttonText.equals("Create")) {
                        String hashedPassword = Security.GeneratePasswordHash(dlgResult.getPassword());
                        jsonResult = objClientLogic.processUser(0, dlgResult.getUsername(), hashedPassword,
                                dlgResult.getAccountTypeId(), dlgResult.getOrgUnitId(), "create");
                        messageDialogTitle = "Create User";
                    }
                    else { // edit
                        String hashedPassword = dlgResult.getPassword(); // password may or may not have changed
                        jsonResult = objClientLogic.processUser(dlgResult.getUserId(), dlgResult.getUsername(),
                                hashedPassword, dlgResult.getAccountTypeId(), dlgResult.getOrgUnitId(), "update");
                        messageDialogTitle = "Update User";
                    }
                    if (! Utilities.isNullOrEmpty(jsonResult)) {
                        JSONAcknowledgement objAcknowledgement = objectMapper.readValue(jsonResult, JSONAcknowledgement.class);
                        if (objAcknowledgement.getSuccess().equals("1")) {
                            JOptionPane.showMessageDialog(parent, objAcknowledgement.getMessage(),
                                    messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(parent, objAcknowledgement.getErrorMessage(),
                                    messageDialogTitle + " Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(parent,
                                "Check server is running!\nContact System Administrator. ",
                                messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (JsonProcessingException jsonProcessingException) {
                    JOptionPane.showMessageDialog(parent,
                            messageDialogTitle + " parsing error!\nContact System Administrator. ",
                            messageDialogTitle + " Message", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("User: UserId: " + dlgResult.getUserId() +
                        ", Account Type: " + dlgResult.getAccountTypeId() +
                        ", Org Unit Id: " + dlgResult.getOrgUnitId() +
                        ", Username: " + dlgResult.getUsername() +
                        ", Password: " + dlgResult.getPassword() +
                        ", Password changed ?: " + dlgResult.getPasswordChanged());
            }
        }
    }
}
