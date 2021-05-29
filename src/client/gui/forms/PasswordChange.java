package client.gui.forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PasswordChange extends JDialog {

    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel usernameLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JTextField usernameTextfield;
    private JPasswordField oldPasswordPwdField;
    private JPasswordField newPasswordPwdField;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel statusTextfield;

    public PasswordChange(final JFrame parent, String title, String username, String btnText, boolean modal) {
        super(parent, title, modal);

        JPanel p1a = new JPanel(new GridLayout(3, 1,0,3));
        p1a.setBorder(new EmptyBorder(5, 10, 0, 0));
        usernameLabel = new JLabel("Username");
        p1a.add(usernameLabel);
        oldPasswordLabel = new JLabel("Password");
        p1a.add(oldPasswordLabel);
        newPasswordLabel = new JLabel("New Password");
        p1a.add(newPasswordLabel);

        JPanel p1b = new JPanel(new GridLayout(3, 10,0, 3));
        p1b.setBorder(new EmptyBorder(4, 10, 0, 0));
        usernameTextfield = new JTextField(15);
        usernameTextfield.setText(username);
        usernameTextfield.setEnabled(false);
        p1b.add(usernameTextfield);
        oldPasswordPwdField = new JPasswordField();
        p1b.add(oldPasswordPwdField);
        newPasswordPwdField = new JPasswordField();
        p1b.add(newPasswordPwdField);

        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(8, 10, 0, 10));
        p1.add(p1a);
        p1.add(p1b);

        JPanel p2a = new JPanel();
        p2a.setBorder(new EmptyBorder(0, 0, 8, 0));
        okButton = new JButton("btnText");
        okButton.setText(btnText);
        p2a.add(okButton);
        cancelButton = new JButton("Cancel");
        p2a.add(cancelButton);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(p2a, BorderLayout.CENTER);
        statusTextfield = new JLabel(" ");
        p2.add(statusTextfield, BorderLayout.NORTH);
        statusTextfield.setForeground(Color.RED);
        statusTextfield.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p2, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        oldPasswordPwdField.requestFocusInWindow(); // set focus on the 'oldPasswordPwdField' field

        newPasswordPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
            // Will detect user hitting ENTER after filling in password
            if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                okButton.doClick(); // simulate clicking 'Send' button
            }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get password change Information - pass back to parent via 'run' method
                Boolean valid = ValidateDialogResult();
                if (valid) {
                    dlgResult = new DialogResult(usernameTextfield.getText(),
                            String.valueOf(oldPasswordPwdField.getPassword()),
                            String.valueOf(newPasswordPwdField.getPassword()),
                            e.getActionCommand());
                    dispose(); // dispose of Dialog form
                }
                else {
                    JOptionPane.showMessageDialog(parent,
                            "There were some errors, check you inputs!",
                            "Validation Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dlgResult != null) {dlgResult.setButtonName("Cancel");}

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
        private String username = "";
        private String oldPassword = "";
        private String newPassword = "";
        private String buttonName = "";

        public DialogResult() { };
        public DialogResult(String username, String oldPassword, String newPassword, String button_name) {
            this.username = username;
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.buttonName = button_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
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
            if ((usernameTextfield.getText() != null
                    && usernameTextfield.getText().length() > 0)
                    && (String.valueOf(oldPasswordPwdField.getPassword()) != null
                    && String.valueOf(oldPasswordPwdField.getPassword()).length() > 0)
                    && (String.valueOf(newPasswordPwdField.getPassword()) != null
                    && String.valueOf(newPasswordPwdField.getPassword()).length() > 0)
            ) {
                valid = true;
            }
            else {
                valid = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }
}
