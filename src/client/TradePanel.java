package client;

import common.models.AssetModel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Note: Used a custom DataModel for the Asset Combobox. This allow more complicated object to be managed
 * by the component. An object is used so an Id and its Name can be retrieved for that object - Asset. The name
 * is viewed in the comboboxes drop-down list and when selected the programmer has access to the it's id.
 *
 * Alternate way is to have access to a list of Assets objects and to search through them once the user has
 * selected an assets name from the combobox to find the asset's id.
*/

public class TradePanel extends JPanel {

    private ITradePanelListener tradePanelListener;

    private JLabel tradeTypeLabel = new JLabel("Trade Type");
    private JComboBox<String> tradeTypeCombobox = new JComboBox<>();
    private JLabel assetLabel = new JLabel("Asset Name");
    private JComboBox<Asset> assetCombobox = new JComboBox<Asset>();
    private JLabel quantityLabel = new JLabel("Quantity");
    private JTextField quantityTextField = new JTextField(4);
    private JLabel creditsLabel = new JLabel("Credits");
    private JTextField creditsTextField = new JTextField(4);
    private JButton submitButton = new JButton("Submit");

    private ArrayList<String> tradeTypes = new ArrayList<>();
    private ArrayList<AssetModel> assetModels = new ArrayList<>();
    private int asset_id;
    private String asset_name;

    TradePanel() {

        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(4,5,1,0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(tradeTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(assetLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.ipadx = 0;
        add(quantityLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.ipadx = 0;
        add(creditsLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(4,5,15,0);
        gbc.anchor = GridBagConstraints.CENTER;
        tradeTypeCombobox.setPreferredSize(new Dimension(65, 22));
        add(tradeTypeCombobox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        AssetComboBoxModel model = new AssetComboBoxModel(loadAssets());
        assetCombobox.setModel(model);
        assetCombobox.setPreferredSize(new Dimension(350, 22));
        add(assetCombobox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        quantityTextField.setPreferredSize(new Dimension(55, 22));
        add(quantityTextField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        creditsTextField.setPreferredSize(new Dimension(55, 22));
        add(creditsTextField, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,5,15,4);
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        assetCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox assetCombobox = (JComboBox) e.getSource();
                Asset asset = (Asset) assetCombobox.getSelectedItem();
                if (asset == null) { return; }
                asset_id = asset.getId();
                asset_name = asset.getAsset_name();

                tradePanelListener.onAssetSelected(asset_id, asset_name);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String asset = assetCombobox.getSelectedItem().toString();
                try {
                    int qty = Integer.valueOf(quantityTextField.getText().trim());
                    int price = Integer.valueOf(creditsTextField.getText().trim());
                    // Check if quantity and price > 0 and are integer values
                    if (qty > 0 && price > 0) {
                        tradePanelListener.onSubmitButtonClick(tradeTypeCombobox.getSelectedItem().toString(),
                                ((Asset) assetCombobox.getSelectedItem()).getId(), asset,
                                Integer.valueOf(quantityTextField.getText().trim()),
                                Integer.valueOf(creditsTextField.getText().trim()));
                    }
                    else {
                        JOptionPane.showMessageDialog(getParent(),
                                "Quantity and Price must be greater than zero!",
                                "Validation Message", JOptionPane.WARNING_MESSAGE);
                    }
                }
                catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Check all trade values are present and correct types before submitting!",
                            "Validation Message", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    } // end constructor

    /**
     *  Used to manage the trading panel's component's properties. Dependent on login status
     * @param status true or false - logged-in or out
     */
    public void enableTradingPanel(boolean status) {
        tradeTypeCombobox.setEnabled(status);
        assetCombobox.setEnabled(status);
        quantityTextField.setEnabled(status);
        if (status) {
            quantityTextField.setBackground(Color.white);
        }
        else {
            quantityTextField.setBackground(getBackground());
        }
        creditsTextField.setEnabled(status);
        if (status) {
            creditsTextField.setBackground(Color.white);
        }
        else {
            creditsTextField.setBackground(getBackground());
        }
        submitButton.setEnabled(status);
    }

    public void setTradeTypes (ArrayList<String> tradeTypes) {
        tradeTypeCombobox.removeAllItems();
        this.tradeTypes = tradeTypes;
        for(String item : tradeTypes) {
            tradeTypeCombobox.addItem(item);
        }
    }

    public void clearTradeTypeCombobox() {
        tradeTypeCombobox.removeAllItems();
    }

    public void clearAssetCombobox() {
        assetCombobox.removeAllItems();
    }

    // Manage ComboBox 'assetCombobox' mainly so we can get an 'asset_id'
    // Need to store an object into the Comboboxes model and then get the part we need
    public void setAssetModels (ArrayList<AssetModel> assetModels) {
        assetCombobox.removeAllItems();
        this.assetModels = assetModels;
        this.asset_id = assetModels.get(0).getAsset_id();
        this.asset_name = assetModels.get(0).getAsset_name();
        Asset[] assets = loadAssets();
        for (Asset asset : assets) {
            assetCombobox.addItem(asset);
        }
    }

    private Asset[] loadAssets() {
        Asset[] assets = new Asset[this.assetModels.size()];
        int i = 0;
        for (AssetModel assetModel : this.assetModels) {
            assets[i++] = new Asset(assetModel.getAsset_id(), assetModel.getAsset_name());
        }
        return assets;
    }

    class AssetComboBoxModel extends DefaultComboBoxModel<Asset> {
        public AssetComboBoxModel(Asset[] assets) {
            super(assets);
        }
        @Override
        public Asset getSelectedItem() {
            Asset selectedAsset = (Asset) super.getSelectedItem();
            // Action can be performed on asset
            return selectedAsset;
        }

    }

    private class Asset {
        private int id;
        private String asset_name;

        public Asset(int id, String asset_name) { this.id = id; this.asset_name = asset_name; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getAsset_name() { return asset_name; }
        public void setAsset_name(String asset_name) { this.asset_name = asset_name; }

        @Override
        public String toString() { return asset_name; }
    }

    public void setTradePanelListener(ITradePanelListener listener) {
        this.tradePanelListener = listener;
    }
}
