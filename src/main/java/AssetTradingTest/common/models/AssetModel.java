package AssetTradingTest.common.models;

import AssetTradingPlatform.common.Asset;

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