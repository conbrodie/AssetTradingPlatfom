package server.dal;

import java.util.List;

public class AssetDbMock implements AssetDb {
    private List<String> getAssetReturns;
    private List<Boolean> createAssetReturn;
    public AssetDbMock(List<String> getAssetReturns, List<Boolean> createAssetReturn) {
        this.getAssetReturns = getAssetReturns;
        this.createAssetReturn = createAssetReturn;
    }
    @Override
    public String getAssets() {
        return getAssetReturns.remove(0);
    }

    @Override
    public boolean createAsset(int asset_id, String asset_name) {
        return createAssetReturn.remove(0);
    }
}
