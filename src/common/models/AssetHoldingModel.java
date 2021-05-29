package common.models;

/**
 * This is POJO model class. It is used to take part in serialisation, de-serialisation and is used to model the
 * data contained with a record of the AssetHolding database table.
 */
public class AssetHoldingModel {
    private int org_unit_id;
    private int asset_id;
    private int quantity;

    public AssetHoldingModel() { }

    public AssetHoldingModel(int org_unit_id, int asset_id, int quantity) {
        this.org_unit_id = org_unit_id;
        this.asset_id = asset_id;
        this.quantity = quantity;
    }

    public int getOrg_unit_id() {
        return org_unit_id;
    }

    public void setOrg_unit_id(int org_unit_id) {
        this.org_unit_id = org_unit_id;
    }

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "[AssetHoldingModel{]" +
                "org_unit_id=" + org_unit_id +
                ", asset_id=" + asset_id +
                ", quantity=" + quantity +
                ']';
    }
}
