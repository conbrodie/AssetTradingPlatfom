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
     * @param id Unique order ID
     * @param assetName
     * @param orderRequest Buy or Sell
     * @param orderAmount Amount of asset in order
     * @param assetValue Specified value of asset in order
     * @throws OrderException Insuffecient credits
     * @throws OrderException User doesnt have access to requested asset (Sell only)
     */
    public Order(){

    }


}
