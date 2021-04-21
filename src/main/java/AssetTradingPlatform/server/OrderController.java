package AssetTradingPlatform.server;

import AssetTradingPlatform.common.Order;
import AssetTradingPlatform.common.OrderRequest;

public class OrderController {
    private AssetTradingPlatform.server.DB DB;

    public OrderController(DB DB) {
        this.DB = DB;
    }

    /**
     * Create New Order
     * @param assetName Asset being traded
     * @param orderRequest Buy or Sell
     * @param orderAmount Amount of asset in order (CAN FRACTIONS OF ASSET BE SOLD?? FLOAT OR INT)
     * @param assetValue Specified cost (Buy/Sell price) of assets in the order
     * @param organisation Specified user's organisation
     * @throws Exception Insufficient credits
     * @throws Exception User doesn't have access to requested asset (Sell only)
     */
    public Order createOrder(String assetName, OrderRequest orderRequest, int orderAmount, int assetValue, String organisation) throws Exception{
        return null;
    }
}