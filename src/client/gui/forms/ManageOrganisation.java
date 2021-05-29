package client.gui.forms;

import common.models.OrgUnitModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ManageOrganisation extends JDialog {
    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel organisationLabel;
    private JLabel creditLabel;
    private JTextField organisationTextField;
    private JComboBox<String> orgUnitComboBox;
    private JTextField creditTextField;
    private JButton actionButton;
    private JButton cancelButton;
    private String buttonName;
    private ArrayList<OrgUnitModel> orgUnits;

    public ManageOrganisation(JFrame parent, String title, String buttonName,
                              boolean modal, ArrayList<OrgUnitModel> orgUnits) {
        super(parent, title, modal);


        this.buttonName = buttonName;
        this.orgUnits = orgUnits;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(20,15,2,0);
        gbc.anchor = GridBagConstraints.WEST;
        organisationLabel = new JLabel("Organisation");
        add(organisationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20,15,2,15);
        gbc.anchor = GridBagConstraints.WEST;
        if (buttonName.equals("Create")) {
            organisationTextField = new JTextField(15);
            add(organisationTextField, gbc);
        }
        else {
            orgUnitComboBox = new JComboBox<String>();
            loadComboBoxItems("org");
            orgUnitComboBox.setPreferredSize(new Dimension(205, 20));
            orgUnitComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String item = (String) orgUnitComboBox.getSelectedItem();
                    // Update the Credit value for the OrgUnit
                    creditTextField.setText(findCreditValue(item));
                    System.out.println(item);
                }
            });
            add(orgUnitComboBox, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(3,15,2,0);
        gbc.anchor = GridBagConstraints.WEST;
        creditLabel = new JLabel("Credits");
        add(creditLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(3,15,2,20);
        gbc.anchor = GridBagConstraints.WEST;
        creditTextField = new JTextField(4);
        add(creditTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(15,15,15,15);
        gbc.anchor = GridBagConstraints.WEST;
        actionButton = new JButton(buttonName);
        actionButton.setPreferredSize(new Dimension(75, 26));
        add(actionButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        add(cancelButton, gbc);

        readyComponents();

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        creditTextField.addKeyListener(new KeyAdapter() {
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
                    if (! buttonName.equals("Create")) {
                        dlgResult = new DialogResult(orgUnitComboBox.getSelectedItem().toString(),
                                getOrgUnitId(orgUnitComboBox.getSelectedItem().toString()),
                                Integer.valueOf(creditTextField.getText()),
                                e.getActionCommand());
                    }
                    else {
                        dlgResult = new DialogResult(organisationTextField.getText(), 0,
                                Integer.valueOf(creditTextField.getText()),
                                e.getActionCommand());
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
        private String orgUnitName;
        private int orgUnitId;
        private int credits;
        private String buttonName;

        public DialogResult() { };

        public DialogResult(String orgUnitName, int orgUnitId, int credits, String buttonName) {
            this.orgUnitName = orgUnitName;
            this.orgUnitId = orgUnitId;
            this.credits = credits;
            this.buttonName = buttonName;
        }

        public String getOrgUnitName() {
            return orgUnitName;
        }

        public void setOrgUnitName(String orgUnitName) {
            this.orgUnitName = orgUnitName;
        }

        public int getOrgUnitId() {
            return orgUnitId;
        }

        public void setOrgUnitId(int orgUnitId) {
            this.orgUnitId = orgUnitId;
        }

        public int getCredits() {
            return credits;
        }

        public void setCredits(int credits) {
            this.credits = credits;
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
            if (buttonName.equals("Create")) {
                if ((String.valueOf(organisationTextField.getText()) != null
                        && String.valueOf(organisationTextField.getText()).length() > 0)
                        && (String.valueOf(creditTextField.getText()) != null
                        && String.valueOf(creditTextField.getText()).length() > 0)
                        && Integer.valueOf(creditTextField.getText()) >= 0) {
                    valid = true;
                } else {
                    valid = false;
                }
            }
            else {
                if ((String.valueOf(creditTextField.getText()) != null
                        && String.valueOf(creditTextField.getText()).length() > 0)
                        && Integer.valueOf(creditTextField.getText()) >= 0) {
                    valid = true;
                } else {
                    valid = false;
                }
            }
        }
        catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    private void readyComponents() {
        if(! buttonName.equals("Create")) {
            creditTextField.setText(String.valueOf(orgUnits.get(0).getCredits()));
        }
        else {
            creditTextField.setText("0");
        }
    }

    private void loadComboBoxItems(String nameOfComboBox) {
        if (nameOfComboBox.equals("org")) {
            for (OrgUnitModel oum : orgUnits ) {
                orgUnitComboBox.addItem(oum.getOrg_unit_name());
            }
        }
    }

    private String findCreditValue(String orgUnitName) {
        int credits = 0;
        for (OrgUnitModel oum : orgUnits ) {
            if (orgUnitName.equals(oum.getOrg_unit_name())) {
                credits = oum.getCredits();
                break;
            }
        }
        return String.valueOf(credits);
    }

    private int getOrgUnitId(String orgUnitName) {
        int id = 0;
        for (OrgUnitModel oum : orgUnits ) {
            if (orgUnitName.equals(oum.getOrg_unit_name())) {
                id = oum.getOrg_unit_id();
                break;
            }
        }
        return id;
    }
}
