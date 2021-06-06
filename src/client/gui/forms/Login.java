package client.gui.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import client.gui.Gbc;

import static client.gui.Gbc.addToPanel;

public class Login extends JDialog {

    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JLabel lblStatus;

    public Login(final JFrame parent, String title, String btnText, boolean modal) {
        super(parent, title, modal);

        // initialise components
        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        txtUsername = new JTextField(20);
        pwdPassword = new JPasswordField(20);
        btnLogin = new JButton(btnText);
        btnCancel = new JButton("Cancel");
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        // create layout
        JPanel p1 = new JPanel(new GridBagLayout());
        addToPanel(p1, lblUsername, Gbc.nu(0,0,1,1).pad(5));
        addToPanel(p1, txtUsername, Gbc.nu(1, 0, 1, 1).pad(5));
        addToPanel(p1, lblPassword, Gbc.nu(0,1,1,1).pad(5));
        addToPanel(p1, pwdPassword, Gbc.nu(1,1,1,1).pad(5));
        addToPanel(p1, lblStatus, Gbc.nu(0,2,2,1).pad(5));

        JPanel p2 = new JPanel(new GridBagLayout());
        addToPanel(p2, btnLogin, Gbc.nu(0, 0, 1,1).pad(5).east().weightX(1));
        addToPanel(p2, btnCancel, Gbc.nu(1,0,1,1).pad(5).west().weightX(1));

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p2, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pwdPassword.addActionListener((e) -> btnLogin.doClick());

        btnLogin.addActionListener((e) -> {
            if (validateDialogResult()) {
                dlgResult = new DialogResult(txtUsername.getText(),
                        String.valueOf(pwdPassword.getPassword()), e.getActionCommand());
                dispose(); // dispose of Dialog form
            } else {
                JOptionPane.showMessageDialog(parent,
                        "There were some errors, check you inputs!",
                        "Validation Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(new ActionListener() {
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

    private boolean validateDialogResult() {
        if (txtUsername.getText().isBlank()) {
            lblStatus.setText("Please enter a username");
            return false;
        }
        if (pwdPassword.getPassword().length == 0) {
            lblStatus.setText("Please enter a password");
            return false;
        }
        lblStatus.setText(" ");
        return true;
    }
}
