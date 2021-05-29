package client.gui.forms;

import common.models.AccountTypeModel;
import common.models.OrgUnitModel;
import common.models.UserModel;
import common.security.Security;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ManageUser extends JDialog {
    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel organisationLabel;
    private JLabel accountTypeLabel;
    private JTextField usernameTextField;
    private JComboBox<String> usernameComboBox;
    private JPasswordField passwordPwdField;
    private JComboBox<String> orgUnitComboBox;
    private JComboBox<String> accountTypeComboBox;
    private JButton pwdChangeButton;
    private JButton actionButton;
    private JButton cancelButton;
    private String buttonName;
    private ArrayList<UserModel> users;
    private ArrayList<OrgUnitModel> orgUnits;
    private ArrayList<AccountTypeModel> accountTypes;
    private String originalHashedPassword;
    private char[] newPassword;
    private boolean passwordChanged;


    public ManageUser(JFrame parent, String title, String buttonName, boolean modal,
                      ArrayList<UserModel> users, ArrayList<OrgUnitModel> orgUnits,
                      ArrayList<AccountTypeModel> accountTypes) {
        super(parent, title, modal);
        this.buttonName = buttonName;
        this.users = users;
        this.orgUnits = orgUnits;
        this.accountTypes = accountTypes;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        if (buttonName.equals("Create")) {
            gbc.insets = new Insets(20, 15, 2, 0);
            usernameLabel = new JLabel("Username");
        } else {
            gbc.insets = new Insets(20, 15, 15, 0);
            usernameLabel = new JLabel("Edit User ");
        }
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        if (buttonName.equals("Create")) {
            gbc.insets = new Insets(20, 15, 2, 15);
            usernameTextField = new JTextField(13);
            add(usernameTextField, gbc);
        } else { // edit
            gbc.insets = new Insets(20, 15, 15, 15);
            usernameComboBox = new JComboBox<String>();
            usernameComboBox.setPreferredSize(new Dimension(210, 20));
            usernameComboBox.setMaximumRowCount(5);
            loadComboBoxItems("user");

            usernameComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String item = (String) usernameComboBox.getSelectedItem();
                    // Update the Password to this users password
                    String password = findUsersPassword(item); // original hashed pwd, will be need if pwd is NOT changed
                    passwordPwdField.setText(password);
                    originalHashedPassword = password; // original hashed pwd, will be need if pwd is NOT changed
                    // Find orgUnitName for this User and set in orgUnitComboBox
                    orgUnitComboBox.setSelectedIndex(getIndexInOrgUnitComboBox(item));
                    // Find AccountTypeName for this User and set it in accountTypeComboBox
                    accountTypeComboBox.setSelectedIndex(getIndexInAccountTypeComboBox(item));
                    System.out.println(item);
                }
            });
            add(usernameComboBox, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(3, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        passwordLabel = new JLabel("Password");
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        passwordPwdField = new JPasswordField(13);
        //passwordPwdField.setEchoChar((char)0);
        add(passwordPwdField, gbc);

        if (! buttonName.equals("Create")) {
            passwordPwdField.setText(users.get(0).getPassword());
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(3, 0, 2, 20);
            gbc.anchor = GridBagConstraints.EAST;
            pwdChangeButton = new JButton("...");
            //passwordPwdField.setEchoChar((char)0);
            pwdChangeButton.setPreferredSize(new Dimension(30, 22));
            add(pwdChangeButton, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(3, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        organisationLabel = new JLabel("Organisation");
        add(organisationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 15, 2, 20);
        gbc.anchor = GridBagConstraints.WEST;
        orgUnitComboBox = new JComboBox<String>();
        loadComboBoxItems("org");
        if (! buttonName.equals("Create")) {
            orgUnitComboBox.setSelectedIndex(getIndexInOrgUnitComboBox(usernameComboBox.getItemAt(0)));
        }
        orgUnitComboBox.setPreferredSize(new Dimension(210, 20));
        add(orgUnitComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(3, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        accountTypeLabel = new JLabel("Account Type");
        add(accountTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(3, 15, 2, 20);
        gbc.anchor = GridBagConstraints.WEST;
        accountTypeComboBox = new JComboBox<String>();
        loadComboBoxItems("account");
        if (! buttonName.equals("Create")) {
            accountTypeComboBox.setSelectedIndex(getIndexInAccountTypeComboBox(usernameComboBox.getItemAt(0)));
        }
        accountTypeComboBox.setPreferredSize(new Dimension(210, 20));
        add(accountTypeComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;
        actionButton = new JButton(buttonName);
        actionButton.setPreferredSize(new Dimension(75, 26));
        add(actionButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 5, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        add(cancelButton, gbc);

        readyComponents();

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        passwordPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                // Will detect user hitting ENTER after filling in password
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionButton.doClick(); // simulate clicking 'Send' button
                }
            }
        });

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get information - pass back to parent via 'run' method
                Boolean valid = ValidateDialogResult();
                if (valid) {
                    if (! buttonName.equals("Create")) { // edit
                        dlgResult = new DialogResult(usernameComboBox.getSelectedItem().toString(),
                                getUserId(usernameComboBox.getSelectedItem().toString()),
                                String.valueOf(passwordPwdField.getPassword()),
                                Integer.valueOf(getOrgUnitId(orgUnitComboBox.getSelectedItem().toString())),
                                Integer.valueOf(getAccountTypeId(accountTypeComboBox.getSelectedItem().toString())),
                                passwordChanged, e.getActionCommand());
                    }
                    else { // create
                        dlgResult = new DialogResult(usernameTextField.getText(), 0,
                                String.valueOf(passwordPwdField.getPassword()),
                                Integer.valueOf(getOrgUnitId(orgUnitComboBox.getSelectedItem().toString())),
                                Integer.valueOf(getAccountTypeId(accountTypeComboBox.getSelectedItem().toString())),
                                false, e.getActionCommand());
                    }
                    dispose(); // dispose of Dialog form
                } else {
                    JOptionPane.showMessageDialog(parent,
                            "There were some errors, check you inputs!",
                            "Validation Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dlgResult != null) {
                    dlgResult.setButtonName("Cancel");
                }

                dispose(); // dispose of Dialog form
            }
        });
    }

    public DialogResult run() {
        // Used to show the Dialog and to return the user's information
        this.setVisible(true);

        return dlgResult;
    }
    /**
     * @class  DialogResult
     * @use Class to store and return form values/settings to calling application
     * @special Customised to suit this form only
     */
    public static class DialogResult {
        private String username;
        private int userId;
        private String password;
        private int orgUnitId;
        private int accountTypeId;
        private boolean passwordChanged;
        private String buttonName;

        public DialogResult() { };

        public DialogResult(String username, int userId,  String password, int orgUnitId,
                            int accountTypeId, boolean passwordChanged, String buttonName) {
            this.username = username;
            this.userId = userId;
            this.password = password;
            this.orgUnitId = orgUnitId;
            this.accountTypeId = accountTypeId;
            this.passwordChanged = passwordChanged;
            this.buttonName = buttonName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getOrgUnitId() {
            return orgUnitId;
        }

        public void setOrgUnitId(int orgUnitId) {
            this.orgUnitId = orgUnitId;
        }

        public int getAccountTypeId() {
            return accountTypeId;
        }

        public void setAccountTypeId(int accountTypeId) {
            this.accountTypeId = accountTypeId;
        }

        public boolean getPasswordChanged() {
            return passwordChanged;
        }

        public void setPasswordChanged(boolean passwordChanged) {
            this.passwordChanged = passwordChanged;
        }

        public String getButtonName() {
            return buttonName;
        }

        public void setButtonName(String buttonName) {
            this.buttonName = buttonName;
        }
    }

    private Boolean  ValidateDialogResult() {
        /**
         * @action Used to validate user's entries
         * @param none
         * @special private method
         * @return boolean - success or failure
         */

        Boolean valid = false;
        try {
            if (! buttonName.equals("Create")) {
                valid = true;
            }
            else {
                if ((usernameTextField.getText() != null
                        && usernameTextField.getText().length() > 0)
                        && (String.valueOf(passwordPwdField.getPassword()) != null
                        && String.valueOf(passwordPwdField.getPassword()).length() > 0)) {

                    valid = true;
                } else {
                    valid = false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }

    private void readyComponents() {
        if(! buttonName.equals("Create")) { // Edit
            if (users.size() < 0) {
                orgUnitComboBox.setEnabled(false);
                orgUnitComboBox.setBackground(Color.decode("#E5E8E8"));
                accountTypeComboBox.setEnabled(false);
                accountTypeComboBox.setBackground(Color.decode("#E5E8E8"));
            }
            passwordPwdField.setEnabled(false);
            passwordPwdField.setBackground(Color.decode("#E5E8E8"));

            loadComboBoxItems("orgUnitComboBox");

            pwdChangeButton.addActionListener(new ActionListener() {
                boolean wasCancelled;
                @Override
                public void actionPerformed(ActionEvent e) {
                    IGetDialogResponse result = new IGetDialogResponse() {
                        @Override
                        public void GetResponse(char[] response, boolean cancelled) {
                            wasCancelled = cancelled; // if user cancelled...
                            if (! wasCancelled) {
                                newPassword = response; // save it to a private variable.
                            }
                        }
                    };

                    // Will execute from here, after the user clicks Change Password Button -the one with '...'
                    char[] oldPwd = passwordPwdField.getPassword(); // Store the OLD password in hashed form
                    System.out.println(oldPwd); // TODO: debug code
                    // Create and display a change password dialog form - uses an interface 'result' to pass out value
                    // Good for this single parameter - new password
                    SetPassword dlg = new SetPassword(ManageUser.this, "Enter New Password", true, result);
                    dlg.setVisible(true); // show dialog >>>> wait for dialog to return >>>>

                    // Will have the contents of the ChangePassword dialog via 'public void GetResponse(char[] response, boolean cancelled) '
                    if (! wasCancelled) {
                        // New password (maybe), user may have entered the old password value, you don't know what was entered
                        char[] newPwd = Security.GeneratePasswordHash(new String(newPassword)).toCharArray(); // entered password
                        if (Arrays.equals(oldPwd, newPwd)) {
                            System.out.println("Password was NOT changed!"); // TODO: debug code
                            passwordPwdField.setText(new String(oldPwd)); // oldPwd holds old password
                            passwordChanged = false;
                        } else {
                            System.out.println("Password WAS changed!"); // TODO: debug code
                            passwordPwdField.setText(new String(newPwd)); // newPwd holds new password
                            passwordChanged = true;
                        }
                        System.out.println(newPassword); // TODO: debug code
                        System.out.println(newPwd); // TODO: debug code
                    }
                }
            });
        } else { /* do nothing */ }
    }

    private void loadComboBoxItems(String nameOfComboBox) {
        // Load all comboboxes on dialog
        if (nameOfComboBox.equals("org")) {
            for (OrgUnitModel oum : orgUnits ) {
                orgUnitComboBox.addItem(oum.getOrg_unit_name());
            }
        }
        else if (nameOfComboBox.equals("account")) {
            for (AccountTypeModel atm : accountTypes ) {
                accountTypeComboBox.addItem(atm.getAccount_type());
            }
        }
        else if (nameOfComboBox.equals("user")) {
            for (UserModel um : users ) {
                usernameComboBox.addItem(um.getUsername());
            }
        }
    }

    private int getUserId(String username) {
        // Get user_id for a passed username
        int userId = 0;
        for (UserModel um : users) {
            if (username.equals(um.getUsername())) {
                userId = um.getUser_id();
                break;
            }
        }
        return userId;
    }

    private int getOrgUnitId(String orgUnitName) {
        // Get org_unit_id for a passed org unit name
        int id = 0;
        for (OrgUnitModel oum : orgUnits ) {
            if (orgUnitName.equals(oum.getOrg_unit_name())) {
                id = oum.getOrg_unit_id();
                break;
            }
        }
        return id;
    }

    private int getAccountTypeId(String accountTypeName) {
        // Get account_type_id for a passed account name
        int id = 0;
        for (AccountTypeModel atm : accountTypes ) {
            if (accountTypeName.equals(atm.getAccount_type())) {
                id = atm.getAccount_type_id();
                break;
            }
        }
        return id;
    }

    private int getIndexInOrgUnitComboBox(String username) {
        // Get an index for a OrgUnit Combobox based on a username.
        int orgNnitId = -1;
        String orgUnitName = null;
        int index = 0;

        // First - Get user's org_unit_id
        for (UserModel um : users)
            if (username.equals(um.getUsername())) {
                orgNnitId = um.getOrg_unit_id();
                break;
        }

        // Second - Get org_unit_name name using org_unit_id
        for (OrgUnitModel oum : orgUnits ) {
            if (orgNnitId == oum.getOrg_unit_id()) {
                orgUnitName = oum.getOrg_unit_name();
                break;
            }
        }

        // Third - Find index in combobox
        for (int i = 0 ; i < orgUnitComboBox.getItemCount(); i++) {
            if (orgUnitComboBox.getItemAt(i).equals(orgUnitName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getIndexInAccountTypeComboBox(String username) {
        // Get an index for a AccountType Combobox based on a username.
        int accTypetId = -1;
        String accTypeName = null;
        int index = 0;

        // First - Get user's account_type_id
        for (UserModel um : users) {
            if (username.equals(um.getUsername())) {
                accTypetId = um.getAccount_type_id();
                break;
            }
        }

        // Second - Get account_type name using account_type_id
        for (AccountTypeModel atm : accountTypes ) {
            if (accTypetId == atm.getAccount_type_id()) {
                accTypeName = atm.getAccount_type();
                break;
            }
        }

        // Third - Find index in combobox
        for (int i = 0 ; i < accountTypeComboBox.getItemCount(); i++) {
            if (accountTypeComboBox.getItemAt(i).equals(accTypeName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private String findUsersPassword(String username) {
        // Get a user's hashed password using their username as a key
        String password = null;
        for (UserModel um : users)
            if (username.equals(um.getUsername())) {
                password = um.getPassword();
                break;
            }
        return password;
    }
}
