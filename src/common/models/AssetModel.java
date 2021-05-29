package common.models;

/**
 * This is POJO model class. It is used to take part in serialisation, de-serialisation and is used to model the
 * data contained with a record of the Asset database table.
 */
public class AssetModel {
    private int asset_id;
    private String asset_name;

    public AssetModel() { }

    public AssetModel(String message) {
        this.asset_name = message;
    }

    public AssetModel(int asset_id, String asset_name) {
        this.asset_id = asset_id;
        this.asset_name = asset_name;
    }

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    @Override
    public String toString() {
        return "[" +
                "asset_id=" + asset_id +
                ", asset_name='" + asset_name + '\'' +
                ']';
    }
}
