package client.gui.forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
public class Login extends JDialog {

    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextfield;
    private JPasswordField passwordPwdField;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel statusTextfield;

    public Login(final JFrame parent, String title, String btnText, boolean modal) {
        super(parent, title, modal);

        JPanel p1a = new JPanel(new GridLayout(2, 1,0,3));
        p1a.setBorder(new EmptyBorder(5, 10, 0, 0));
        usernameLabel = new JLabel("Username");
        p1a.add(usernameLabel);
        passwordLabel = new JLabel("Password");
        p1a.add(passwordLabel);

        JPanel p1b = new JPanel(new GridLayout(2, 10,0, 3));

        p1b.setBorder(new EmptyBorder(4, 10, 0, 0));
        usernameTextfield = new JTextField(15);
        usernameTextfield.setText("stock.c"); // TODO: debug code
        p1b.add(usernameTextfield);
        passwordPwdField = new JPasswordField();
        passwordPwdField.setText("pwd"); // TODO: debug code
        p1b.add(passwordPwdField);

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

        passwordPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                // Will detect user hitting ENTER after filling in Username
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    okButton.doClick(); // simulate clicking 'Send' button
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get login Information - pass back to parent via 'run' method
                Boolean valid = ValidateDialogResult();
                if (valid) {
                    dlgResult = new DialogResult(usernameTextfield.getText(),
                            String.valueOf(passwordPwdField.getPassword()), e.getActionCommand());
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
        private String username;
        private String password;
        private String buttonName;

        public DialogResult() { };
        public DialogResult(String username, String password, String buttonName) {
            this.username = username;
            this.password = password;
            this.buttonName = buttonName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
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
                    && (String.valueOf(passwordPwdField.getPassword()) != null
                    && String.valueOf(passwordPwdField.getPassword()).length() > 0)) {

                valid = true;
            }
            else {
                valid = false;
                valid = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }
}
