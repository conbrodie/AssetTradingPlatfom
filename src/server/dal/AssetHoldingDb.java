package server.dal;

public interface AssetHoldingDb {
    String getAssetHoldings();
    boolean createAssetHolding(int org_unit_id, int asset_id, int quantity);
    boolean updateAssetHolding(int org_unit_id, int asset_id, int quantity);
}
