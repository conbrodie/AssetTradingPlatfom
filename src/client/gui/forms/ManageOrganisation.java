package client.gui.forms;

import common.models.OrgUnitModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import client.gui.Gbc;

import static client.gui.Gbc.addToPanel;

public class ManageOrganisation extends JDialog {
    private boolean create;
    private DialogResult dlgResult = null; // used to pass form data back

    private JLabel lblOrganisation;
    private JLabel lblCredit;
    private JTextField txtOrganisation;
    private JComboBox<String> cboOrganisation;
    private JTextField txtCredit;
    private JButton btnUpdate;
    private JButton btnCancel;
    private ArrayList<OrgUnitModel> orgUnits;

    public ManageOrganisation(JFrame parent, String title, String buttonName,
                              boolean modal, ArrayList<OrgUnitModel> orgUnits) {
        super(parent, title, modal);

        this.orgUnits = orgUnits;
        // TODO: pass value in better
        this.create = buttonName.equals("Create");

        // initialise components
        lblOrganisation = new JLabel("Organisation");
        txtOrganisation = new JTextField(15);
        cboOrganisation = new JComboBox<String>();
        lblCredit = new JLabel("Credits");
        txtCredit = new JTextField(4);
        btnCancel = new JButton("Cancel");
        btnUpdate = new JButton(buttonName);

        // load values
        if (!create) {
            loadComboBoxItems("org");
        }
        readyComponents();

        // add action listeners
        if (!create) {
            cboOrganisation.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String item = (String) cboOrganisation.getSelectedItem();
                    // Update the Credit value for the OrgUnit
                    txtCredit.setText(findCreditValue(item));
                    System.out.println(item);
                }
            });
        }
        txtCredit.addActionListener((e) -> btnUpdate.doClick());

        btnUpdate.addActionListener((e) -> {
            // Get information - pass back to parent via 'run' method
            if (validateDialogResult()) {
                if (!create) {
                    dlgResult = new DialogResult(cboOrganisation.getSelectedItem().toString(),
                            getOrgUnitId(cboOrganisation.getSelectedItem().toString()),
                            Integer.valueOf(txtCredit.getText()),
                            e.getActionCommand());
                }
                else {
                    dlgResult = new DialogResult(txtOrganisation.getText(), 0,
                            Integer.valueOf(txtCredit.getText()),
                            e.getActionCommand());
                }
                dispose(); // dispose of Dialog form
            } else {
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

        // layout components
        JPanel pnlTop = new JPanel(new GridBagLayout());
        Gbc leftGbc = Gbc.nu().pad(5).lineStart();
        addToPanel(pnlTop, lblOrganisation, leftGbc.xy(0,0,1,1));
        addToPanel(pnlTop, create? txtOrganisation : cboOrganisation, leftGbc.xy(1, 0, 1,1 ).weightX(1));
        addToPanel(pnlTop, lblCredit, leftGbc.xy(0, 1, 1, 1));
        addToPanel(pnlTop, txtCredit, leftGbc.xy(1, 1, 1, 1).weightX(1));

        JPanel pnlBottom = new JPanel(new GridBagLayout());
        addToPanel(pnlBottom, btnUpdate, Gbc.nu(0, 0, 1, 1).pad(5).east().weightX(1));
        addToPanel(pnlBottom, btnCancel, Gbc.nu(1, 0, 1, 1).pad(5).west().weightX(1));

        setLayout(new BorderLayout());
        add(pnlTop, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

    private boolean validateDialogResult() {
        if (create && txtOrganisation.getText().isBlank()) {
            return false;
        }
        if (!txtCredit.getText().matches("^\\d+$")) {
            return false;
        }
        return true;
    }

    private void readyComponents() {
        txtCredit.setText(create ? "0" : "" + orgUnits.get(0).getCredits());
    }

    private void loadComboBoxItems(String nameOfComboBox) {
        if (nameOfComboBox.equals("org")) {
            for (OrgUnitModel oum : orgUnits ) {
                cboOrganisation.addItem(oum.getOrg_unit_name());
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
