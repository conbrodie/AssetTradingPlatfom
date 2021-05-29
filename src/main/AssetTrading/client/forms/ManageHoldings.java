package AssetTrading.client.forms;

import AssetTrading.client.Gbc;
import AssetTrading.common.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static AssetTrading.client.Gbc.addToPanel;


public class ManageHoldings extends JDialog implements ActionListener {
    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel lblOrganisation;
    private JLabel lblAsset;
    private JLabel lblQuantity;
    private JComboBox<String> cboOrganisation;
    private JComboBox<String> cboAsset;
    private JTextField txtQuantity;
    private JButton btnUpdate;
    private JButton btnCancel;
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

        // initialise components
        lblOrganisation = new JLabel("Organisation");
        cboOrganisation = new JComboBox<String>();
        cboOrganisation.setPreferredSize(new Dimension(210, 20));
        lblAsset = new JLabel("Asset");
        cboAsset = new JComboBox<String>();
        cboAsset.setPreferredSize(new Dimension(210, 20));
        lblQuantity = new JLabel("Quantity");
        txtQuantity = new JTextField(6);
        btnUpdate = new JButton(buttonName);
        btnCancel = new JButton("Cancel");

        // load values
        loadComboBoxItems("org");
        loadComboBoxItems("asset");
        setQuantity();

        // add listeners
        cboOrganisation.addActionListener(this);
        cboAsset.addActionListener(this);

        // layout components
        JPanel pnlTop = new JPanel(new GridBagLayout());
        Gbc leftGbc = Gbc.nu().pad(5).lineStart();
        addToPanel(pnlTop, lblOrganisation, leftGbc.xy(0,0, 1,1));
        addToPanel(pnlTop, cboOrganisation, leftGbc.xy(1, 0, 1, 1).weightX(1));
        addToPanel(pnlTop, lblAsset, leftGbc.xy(0, 1, 1, 1));
        addToPanel(pnlTop, cboAsset, leftGbc.xy(1, 1, 1, 1).weightX(1));
        addToPanel(pnlTop, lblQuantity, leftGbc.xy(0, 2, 1, 1));
        addToPanel(pnlTop, txtQuantity, leftGbc.xy(1, 2, 1,1).weightX(1));
        JPanel pnlBottom = new JPanel(new GridBagLayout());
        addToPanel(pnlBottom, btnUpdate, Gbc.nu(0, 0, 1, 1).pad(5).east().weightX(1));
        addToPanel(pnlBottom, btnCancel, Gbc.nu(1, 0, 1, 1).pad(5).west().weightX(1));
        setLayout(new BorderLayout());
        add(pnlTop, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtQuantity.addActionListener((e) -> btnUpdate.doClick());

        btnUpdate.addActionListener((e) -> {
            // Get information - pass back to parent via 'run' method
            if (validateDialogResult()) {
                if ( Integer.valueOf(txtQuantity.getText()) == 0 ) {
                    // Check if user accepts zero quantity of assets for holding
                    int response = JOptionPane.showConfirmDialog(parent, "Asset quantity is zero (0) - Are you sure?",
                            "Validation Message", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        dlgResult = new DialogResult(getOrgUnitId(cboOrganisation.getSelectedItem().toString()),
                                cboOrganisation.getSelectedItem().toString(),
                                getAssetId(cboAsset.getSelectedItem().toString()),
                                cboAsset.getSelectedItem().toString(),
                                Integer.valueOf(txtQuantity.getText()),
                                isCreate, e.getActionCommand());
                        dispose(); // dispose of Dialog form
                    }
                }
                else {
                    dlgResult = new DialogResult(getOrgUnitId(cboOrganisation.getSelectedItem().toString()),
                            cboOrganisation.getSelectedItem().toString(),
                            getAssetId(cboAsset.getSelectedItem().toString()),
                            cboAsset.getSelectedItem().toString(),
                            Integer.valueOf(txtQuantity.getText()),
                            isCreate, e.getActionCommand());
                    dispose(); // dispose of Dialog form
                }
            }
            else {
                JOptionPane.showMessageDialog(parent,
                        "There were some errors, check you inputs!",
                        "Validation Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener((e) -> {
            if (dlgResult != null) {
                dlgResult.setButtonName("Cancel");
            }

            dispose(); // dispose of Dialog form
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

    private boolean validateDialogResult() {
        // check if quantity is not a number
        if (!txtQuantity.getText().matches("^\\d+$")) {
            return false;
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        // This action listener manages both orgUnit and asset comboboxes.
        setQuantity();
    }

    private void setQuantity() {
        if (isAlreadyHolingAsset(cboOrganisation.getSelectedItem().toString(), cboAsset.getSelectedItem().toString())) {
            // If the asset is already allocated to the organisation the user can only update the quantity
            txtQuantity.setText(String.valueOf(findQuantity(cboOrganisation.getSelectedItem().toString(), cboAsset.getSelectedItem().toString()))) ;
            isCreate = false;
            btnUpdate.setText("Update");
        }
        else {
            // Organisation does not own the asset so it can be allocated
            txtQuantity.setText("0");
            isCreate = true;
            btnUpdate.setText("Create");
        }
    }

    private void loadComboBoxItems(String nameOfComboBox) {
        // Load combobox items
        if (nameOfComboBox.equals("org")) {
            for (OrgUnitModel oum : orgUnits ) {
                cboOrganisation.addItem(oum.getOrg_unit_name());
            }
        }
        else if (nameOfComboBox.equals("asset")) {
            for (AssetModel a : assets ) {
                cboAsset.addItem(a.getAsset_name());
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
