package server.dal;

public class AssetHoldingDbMock implements  AssetHoldingDb {

    private String getAssetHoldingsReturn;
    private Boolean createAssetHoldingReturn;
    private Boolean updateAssetHoldingReturn;

    public AssetHoldingDbMock(String getAssetHoldingsReturn, Boolean createAssetHoldingReturn, Boolean updateAssetHoldingReturn) {
        this.getAssetHoldingsReturn = getAssetHoldingsReturn;
        this.createAssetHoldingReturn = createAssetHoldingReturn;
        this.updateAssetHoldingReturn = updateAssetHoldingReturn;
    }

    @Override
    public String getAssetHoldings() {
        return getAssetHoldingsReturn;
    }

    @Override
    public boolean createAssetHolding(int org_unit_id, int asset_id, int quantity) {
        return createAssetHoldingReturn;
    }

    @Override
    public boolean updateAssetHolding(int org_unit_id, int asset_id, int quantity) {
        return updateAssetHoldingReturn;
    }
}
