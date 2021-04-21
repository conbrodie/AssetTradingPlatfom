package AssetTradingPlatform.common;

import AssetTradingPlatform.common.OrderRequest;

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
    public int orderID;
    public LocalDateTime timeCreated;
    public String assetName;
    public OrderRequest orderStatus;
    public int orderAmount;
    public int assetValue;



    /**
     * Create New Order
     *
     * @param orderID      Unique order ID
     * @param assetName    Asset being traded
     * @param orderRequest Buy or Sell
     * @param orderAmount  Amount of asset in order (CAN FRACTIONS OF ASSET BE SOLD?? FLOAT OR INT)
     * @param assetValue   Specified cost (Buy/Sell price) of assets in the order
     * @throws Exception Insufficient credits
     * @throws Exception User doesn't have access to requested asset (Sell only)
     */
    public Order(int orderID, LocalDateTime timeCreated, String assetName, OrderRequest orderStatus, int orderAmount, int assetValue) throws Exception {

    }

}