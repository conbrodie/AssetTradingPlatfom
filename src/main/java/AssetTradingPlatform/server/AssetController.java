package AssetTradingPlatform.server;

import AssetTradingPlatform.common.Asset;

public class AssetController {
    private final AssetTradingPlatform.server.DB DB;

    /**
     * Create a asset management object
     * @param DB
     */
    public AssetController(AssetTradingPlatform.server.DB DB) { this.DB = DB;}
    public Asset createAsset (String asset_name, int price, int quantity, String org_unit_name){
        return null;
    }
}
