package client.gui.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class SetPassword extends JDialog {

    // Use an Interface to return the new password as a char[]

    private JLabel passwordNewALabel;
    private JLabel passwordNewBLabel;
    private JPasswordField passwordNewAPwdField;
    private JPasswordField passwordNewBPwdField;
    private JButton actionButton;
    private JButton cancelButton;

    public SetPassword(JDialog parent, String title, boolean model, IGetDialogResponse result) {
        super(parent, title, model);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        passwordNewALabel = new JLabel("New Password");
        gbc.insets = new Insets(20, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordNewALabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        passwordNewAPwdField = new JPasswordField(15);
        //passwordNewAPwdField.setEchoChar((char)0);
        gbc.insets = new Insets(20, 15, 2, 15);
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordNewAPwdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        passwordNewBLabel = new JLabel("Re-enter Password");
        gbc.insets = new Insets(2, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordNewBLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        passwordNewBPwdField = new JPasswordField(15);
        //passwordNewBPwdField.setEchoChar((char)0);
        gbc.insets = new Insets(2, 15, 2, 15);
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordNewBPwdField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        actionButton = new JButton("Update");
        actionButton.setPreferredSize(new Dimension(75, 26));
        add(actionButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 5, 15, 15);
        gbc.anchor = GridBagConstraints.EAST;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        add(cancelButton, gbc);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Window listeners
        // Needed to show Login dialog at start-up
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelButton.doClick(); // this will close the dialog as if the Cancel button was clicked - Cancelled = true
            }
        });

        passwordNewBPwdField.addKeyListener(new KeyAdapter() {
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
                if (! validatePasswordValues()) {
                    JOptionPane.showMessageDialog(parent,
                            "There were some errors, check you inputs!",
                            "Validation Message", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    // Check passwords are the same
                    char[] newA = passwordNewAPwdField.getPassword(); // must change to byte array to compare
                    char[] newB = passwordNewBPwdField.getPassword(); // must change to byte array to compare
                    if (Arrays.equals(newA, newB)) {
                        result.GetResponse(passwordNewAPwdField.getPassword(), false); // get byte[] from password field
                        Arrays.fill(newA, '0'); // zero out for security
                        Arrays.fill(newB, '0');
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(SetPassword.this,
                                "Passwords do not match or have no entries!",
                                "New Password Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            // Do nothing close dialog
            @Override
            public void actionPerformed(ActionEvent e) {
                result.GetResponse(passwordNewAPwdField.getPassword(), true); // get byte[] from password field
                dispose();
            }
        });
    }
    private boolean validatePasswordValues() {
        // Check the password fields have entries

        if ((String.valueOf(passwordNewAPwdField.getPassword()) != null
                && String.valueOf(passwordNewAPwdField.getPassword()).length() > 0)
                && (String.valueOf(passwordNewBPwdField.getPassword()) != null
                && String.valueOf(passwordNewBPwdField.getPassword()).length() > 0)) {
            return true;
        }
        return false;
    }
}
