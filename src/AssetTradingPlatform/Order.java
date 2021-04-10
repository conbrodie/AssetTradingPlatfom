package AssetTradingPlatform;

import java.time.LocalDateTime;

/**
 * A class to create a new order
 */
public class Order {
    /**
     * @param timeCreated Time of listing
     * @param userCreated User who created listing
     * @param orderStatus Active(Unfulfilled) or Inactive(Fulfilled)
     * @param unitName Which unit the order is associated with (Unit of user)
     */
    public LocalDateTime timeCreated;
    public String userCreated;
    public boolean orderStatus = true;
    public String unitName;


    /**
     * Create New Order
     * @param orderID Unique order ID
     * @param assetName Asset being traded
     * @param orderRequest Buy or Sell
     * @param orderAmount Amount of asset in order (CAN FRACTIONS OF ASSET BE SOLD?? FLOAT OR INT)
     * @param assetValue Specified cost (Buy/Sell price) of assets in the order
     * @throws Exception Insufficient credits
     * @throws Exception User doesn't have access to requested asset (Sell only)
     */
    public Order(int orderID, String assetName, boolean orderRequest, float orderAmount, float assetValue) throws Exception{

    }
}
