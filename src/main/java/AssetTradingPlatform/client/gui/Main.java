package AssetTradingPlatform.client.gui;


// import AssetTradingPlatform.client.gui.panels.ChangePassword;
import AssetTradingPlatform.client.gui.panels.Login;
import AssetTradingPlatform.client.gui.panels.OrganisationUnits;
import AssetTradingPlatform.client.gui.panels.Register;
import AssetTradingPlatform.client.gui.panels.Trade;
import AssetTradingPlatform.client.gui.sidebar.Sidebar;
import AssetTradingPlatform.client.gui.titlebar.TitleBar;
import AssetTradingPlatform.common.Order;
import AssetTradingPlatform.common.OrderRequest;
import AssetTradingPlatform.common.Unit;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends JFrame implements Runnable {

    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 900;

    private JPanel pnlNorth;
    private JTree sidebar;

    private JPanel pnlCenter;
    private JPanel pnlCards;
    private JPanel pnlLogin;
    private JPanel pnlRegister;
    private OrganisationUnits pnlOrgUnits;
    private Trade pnlTrade;


    private void createGui() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout mainLayout = new BorderLayout();
        setLayout(mainLayout);
        // create main panel
        pnlCenter = createPanel(GuiColours.CONTENT);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // create Login panel
        pnlLogin = new Login();
        // create Register panel
        pnlRegister = new Register();
        // create Organisation Units panel
        pnlOrgUnits = new OrganisationUnits();
        ArrayList<Unit> unitList = new ArrayList<>();
        unitList.add(new Unit("ComputeClusterDivision", 5000));
        unitList.add(new Unit("Software Access Management", 2466));
        unitList.add(new Unit("HR Management", 1500));
        //create Trade Panel
        pnlTrade = new Trade();
        ArrayList<Order> orders = new ArrayList<>();
        try {
            orders.add(new Order(OrderRequest.BUY, "test", 5, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }



        for (Order order : orders) {
            System.out.println( order.getAsset_name());
        }


        pnlOrgUnits.setData(unitList);
        pnlTrade.setData(orders);



        pnlOrgUnits.addActionListener(event -> {
            System.out.println("Show org unit " + event.getActionCommand());
        });

        //create panel around edges
        sidebar = new Sidebar();
        getContentPane().add(sidebar, BorderLayout.WEST);
        pnlNorth = new TitleBar();
        pnlNorth.setPreferredSize(new Dimension(FRAME_WIDTH, 50));
        getContentPane().add(pnlNorth, BorderLayout.NORTH);

        // make cards
        pnlCards = new JPanel();
        pnlCards.setLayout(new CardLayout());
        pnlCards.add(pnlLogin, "Login");
        pnlCards.add(pnlRegister, "Register");
        pnlCards.add(pnlOrgUnits, "Organisation Units");
        pnlCards.add(pnlTrade, "Trade");


        //position components in the panel
        pnlCenter.setLayout((new GridBagLayout()));
        addToPanel(pnlCenter, pnlCards, Gbc.nu( 0, 0, 1, 1));
        pack();
        setVisible(true);

        sidebar.addTreeSelectionListener(evt -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    sidebar.getLastSelectedPathComponent();

            /* if nothing is selected */
            if (node == null) return;

            /* retrieve the node that was selected */
            String name = (String)node.getUserObject();
            ((CardLayout)pnlCards.getLayout()).show(pnlCards, name);
        });
    }

    private JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }


    private void addToPanel(JPanel jp, Component c, Gbc g) {
        jp.add(c, g.build());
    }

    @Override
    public void run() {
        createGui();
    }

    public static void main(String[] args) {
        Main frameLogin = new Main();
        SwingUtilities.invokeLater(frameLogin);
    }

}
