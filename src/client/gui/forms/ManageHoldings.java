package client.gui.forms;

import client.MainFrame;
import common.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ManageHoldings extends JDialog implements ActionListener {
    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel organisationLabel;
    private JLabel assetLabel;
    private JLabel quantityLabel;
    private JComboBox<String> orgUnitComboBox;
    private JComboBox<String> assetComboBox;
    private JTextField quantityTextField;
    private JButton actionButton;
    private JButton cancelButton;
    private String buttonName;
    private ArrayList<OrgUnitModel> orgUnits;
    private ArrayList<AssetModel> assets;
    private ArrayList<AssetHoldingModel> holdings;

    private boolean isCreate = false;

    public ManageHoldings(JFrame parent, String title, String buttonText, boolean modal,
                                                  ArrayList<OrgUnitModel> orgUnits, ArrayList<AssetModel> assets,
                                                  ArrayList<AssetHoldingModel> holdings) {
        super(parent, title, modal);
        this.orgUnits = orgUnits;
        this.assets = assets;
        this.holdings = holdings;
        this.buttonName = buttonText;

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        organisationLabel = new JLabel("Organisation");
        gbc.insets = new Insets(20, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(organisationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        orgUnitComboBox = new JComboBox<String>();
        orgUnitComboBox.setPreferredSize(new Dimension(210, 20));
        loadComboBoxItems("org");
        orgUnitComboBox.addActionListener(this);
        // TODO: Check if required - see line 301
//        orgUnitComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                quantityTextField.setText("0");
//            }
//        });
        gbc.insets = new Insets(20, 15, 2, 15);
        gbc.anchor = GridBagConstraints.WEST;
        add(orgUnitComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        assetLabel = new JLabel("Asset");
        gbc.insets = new Insets(2, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(assetLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        assetComboBox = new JComboBox<String>();
        assetComboBox.setPreferredSize(new Dimension(210, 20));
        loadComboBoxItems("asset");
        assetComboBox.addActionListener(this);
        //TODO: Check if this is still required - action shifted to a Dialog wide Listener - see line 301
//        assetComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (isAlreadyHolingAsset(orgUnitComboBox.getSelectedItem().toString(), assetComboBox.getSelectedItem().toString())) {
//                    // Warn user asset already already owned by orgUnit
//                    quantityTextField.setText(String.valueOf(findQuantity(orgUnitComboBox.getSelectedItem().toString(), assetComboBox.getSelectedItem().toString()))) ;
//                    System.out.println("Has this holding!");
//                }
//                else {
//                    quantityTextField.setText("0");
//                }
//            }
//        });

        gbc.insets = new Insets(2, 15, 2, 15);
        gbc.anchor = GridBagConstraints.WEST;
        add(assetComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        quantityLabel = new JLabel("Quantity");
        gbc.insets = new Insets(2, 15, 2, 15);
        gbc.anchor = GridBagConstraints.WEST;
        add(quantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        quantityTextField = new JTextField(6);
        gbc.insets = new Insets(2, 15, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(quantityTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 0);
        gbc.anchor = GridBagConstraints.WEST;
        actionButton = new JButton(buttonName);
        actionButton.setPreferredSize(new Dimension(75, 26));
        add(actionButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 5, 15, 15);
        gbc.anchor = GridBagConstraints.EAST;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(75, 26));
        add(cancelButton, gbc);

        assetComboBox.setSelectedIndex(0);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        quantityTextField.addKeyListener(new KeyAdapter() {
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
                    if ( Integer.valueOf(quantityTextField.getText()) == 0 ) {
                        // Check if user accepts zero quantity of assets for holding
                        int response = JOptionPane.showConfirmDialog(parent, "Asset quantity is zero (0) - Are you sure?",
                                "Validation Message", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            dlgResult = new DialogResult(getOrgUnitId(orgUnitComboBox.getSelectedItem().toString()),
                                    orgUnitComboBox.getSelectedItem().toString(),
                                    getAssetId(assetComboBox.getSelectedItem().toString()),
                                    assetComboBox.getSelectedItem().toString(),
                                    Integer.valueOf(quantityTextField.getText()),
                                    isCreate, e.getActionCommand());
                            dispose(); // dispose of Dialog form
                        }
                    }
                    else {
                        dlgResult = new DialogResult(getOrgUnitId(orgUnitComboBox.getSelectedItem().toString()),
                                orgUnitComboBox.getSelectedItem().toString(),
                                getAssetId(assetComboBox.getSelectedItem().toString()),
                                assetComboBox.getSelectedItem().toString(),
                                Integer.valueOf(quantityTextField.getText()),
                                isCreate, e.getActionCommand());
                        dispose(); // dispose of Dialog form
                    }
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
     * @class DialogResult
     * @use Class to store and return form values/settings to calling application
     * @special Customised to suit this form only
     */
    public static class DialogResult {

        private int orgUnitId;
        private String orgUnitName;
        private int assetId;
        private String assetName;
        private int quantity;
        private boolean isCreate;
        private String buttonName;

        public DialogResult() {
        }

        public DialogResult(int orgUnitId, String orgUnitName, int assetId, String assetName, int quantity, boolean isCreate, String buttonName) {
            this.orgUnitId = orgUnitId;
            this.orgUnitName = orgUnitName;
            this.assetId = assetId;
            this.assetName = assetName;
            this.quantity = quantity;
            this.isCreate = isCreate;
            this.buttonName = buttonName;
        }

        public int getOrgUnitId() {
            return orgUnitId;
        }

        public void setOrgUnitId(int orgUnitId) {
            this.orgUnitId = orgUnitId;
        }

        public String getOrgUnitName() {
            return orgUnitName;
        }

        public void setOrgUnitName(String orgUnitName) {
            this.orgUnitName = orgUnitName;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public int getAssetId() {
            return assetId;
        }

        public void setAssetId(int assetId) {
            this.assetId = assetId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public boolean isCreate() {
            return isCreate;
        }

        public void setCreate(boolean create) {
            isCreate = create;
        }

        public String getButtonName() {
            return buttonName;
        }

        public void setButtonName(String buttonName) {
            this.buttonName = buttonName;
        }
    }

    private Boolean ValidateDialogResult() {
        /**
         * @action Used to validate user's entries
         * @param none
         * @special private method
         * @return boolean - success or failure
         */

        Boolean valid = false;
        try {
            // This is the only variable to be validated...
            if ((String.valueOf(quantityTextField.getText()) != null
                    && String.valueOf(quantityTextField.getText()).length() > 0)
                    && Integer.valueOf(quantityTextField.getText()) >= 0) { // allowed to have zero quantity of asset
                valid = true;
            } else {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    public void actionPerformed(ActionEvent e) {
        // This action listener manages both orgUnit and asset comboboxes.
        if (isAlreadyHolingAsset(orgUnitComboBox.getSelectedItem().toString(), assetComboBox.getSelectedItem().toString())) {
            // If the asset is already allocated to the organisation the user can only update the quantity
            quantityTextField.setText(String.valueOf(findQuantity(orgUnitComboBox.getSelectedItem().toString(), assetComboBox.getSelectedItem().toString()))) ;
            isCreate = false;
            actionButton.setText("Update");
        }
        else {
            // Organisation does not own the asset so it can be allocated
            quantityTextField.setText("0");
            isCreate = true;
            actionButton.setText("Create");
        }
    }

    private void loadComboBoxItems(String nameOfComboBox) {
        // Load combobox items
        if (nameOfComboBox.equals("org")) {
            for (OrgUnitModel oum : orgUnits ) {
                orgUnitComboBox.addItem(oum.getOrg_unit_name());
            }
        }
        else if (nameOfComboBox.equals("asset")) {
            for (AssetModel a : assets ) {
                assetComboBox.addItem(a.getAsset_name());
            }
        }
    }

    private ArrayList<AssetModel>  getAssetBelongingToOrgUnit(String OrgUnitName) {
        // Get all assets allocated to a organisation
        ArrayList<AssetModel> assets = new ArrayList<>();
        for (AssetModel a : assets) {
            if (isAlreadyHolingAsset(OrgUnitName, a.getAsset_name())) {
                assets.add(a);
            }
        }
        return assets;
    }

    private boolean isAlreadyHolingAsset(String OrgUnitName, String assetName) {

        boolean found = false;

        // Get OrgUnitId
        int orgUnitId = getOrgUnitId(OrgUnitName);
        int assetId = getAssetId(assetName);
        for (AssetHoldingModel ahm : holdings) {
            if (orgUnitId == ahm.getOrg_unit_id() && assetId == ahm.getAsset_id()) {
                found = true;
                break;
            }
        }
        return found;
    }

    private int getOrgUnitId(String orgUnitName) {
        // Get orgUnitId is from its name
        int id = 0;
        for (OrgUnitModel oum : orgUnits)
            if (orgUnitName.equals(oum.getOrg_unit_name())) {
                id = oum.getOrg_unit_id();
                break;
            }
        return id;
    }

    private int getAssetId(String assetName) {
        // Get assetId from its name
        int id = 0;
        for (AssetModel a : assets)
            if (assetName.equals(a.getAsset_name())) {
                id = a.getAsset_id();
                break;
            }
        return id;
    }

    private int findQuantity(String OrgUnitName, String assetName) {
        // Find quantity of asset held by an organisation
        int quantity = 0;

        // Get quantity of assets held by this organisation
        int orgUnitId = getOrgUnitId(OrgUnitName);
        int assetId = getAssetId(assetName);
        for (AssetHoldingModel ahm : holdings) {
            if (orgUnitId == ahm.getOrg_unit_id() && assetId == ahm.getAsset_id()) {
                quantity = ahm.getQuantity();
                break;
            }
        }
        return quantity;
    }
}
