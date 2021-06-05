package server.dal;

import java.util.List;

public class AssetDbMock implements AssetDb {
    private String getAssetReturn;
    private boolean createAssetReturn;
    public AssetDbMock(String getAssetReturn, boolean createAssetReturn) {
        this.getAssetReturn = getAssetReturn;
        this.createAssetReturn = createAssetReturn;
    }
    @Override
    public String getAssets() {
        return getAssetReturn;
    }

    @Override
    public boolean createAsset(int asset_id, String asset_name) {
        return createAssetReturn;
    }
}
