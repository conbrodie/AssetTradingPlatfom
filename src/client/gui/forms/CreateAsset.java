package client.gui.forms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class CreateAsset extends JDialog {

    private DialogResult dlgResult = null; // used to pass form data back
    private JLabel assetNameLabel;
    private JTextField assetNameTextField;
    private JButton actionButton;
    private JButton cancelButton;

    public CreateAsset(final JFrame parent, String title, String buttonText, boolean modal) {
        super(parent, title, modal);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(20,15,2,0);
        gbc.anchor = GridBagConstraints.WEST;
        assetNameLabel = new JLabel("Asset Name");
        add(assetNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,15,2,15);
        gbc.anchor = GridBagConstraints.WEST;
        assetNameTextField = new JTextField(15);
        add(assetNameTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(15,15,15,15);
        gbc.anchor = GridBagConstraints.WEST;
        actionButton = new JButton(buttonText);
        actionButton.setPreferredSize(new Dimension(75, 26));
        add(actionButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        add(cancelButton, gbc);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        assetNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                // Will detect user hitting ENTER after filling in asset name
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionButton.doClick(); // simulate clicking 'Send' button
                }
            }
        });

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get asset information - pass back to parent via 'run' method
                Boolean valid = ValidateDialogResult();
                if (valid) {
                    dlgResult = new DialogResult(0, assetNameTextField.getText(), e.getActionCommand());
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
        private int assetId;
        private String assetName;
        private String buttonName;

        public DialogResult() { };

        public DialogResult(int assetId, String assetName, String buttonName) {
            this.assetId = assetId;
            this.assetName = assetName;
            this.buttonName = buttonName;
        }

        public int getAssetId() {
            return assetId;
        }

        public void setAssetId(int assetId) {
            this.assetId = assetId;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setUsername(String username) {
            this.assetName = assetName;
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
            if ((assetNameTextField.getText() != null
                    && assetNameTextField.getText().length() > 0)
                    && (String.valueOf(assetNameTextField.getText()) != null
                    && String.valueOf(assetNameTextField.getText()).length() > 0)) {

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
