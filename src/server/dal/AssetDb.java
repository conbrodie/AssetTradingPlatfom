package server.dal;

public interface AssetDb {
    String getAssets();
    boolean createAsset(int asset_id, String asset_name);
}
