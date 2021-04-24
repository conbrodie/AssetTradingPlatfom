package AssetTradingPlatform.common;

import AssetTradingPlatform.common.OrderRequest;

import java.time.LocalDateTime;

/**
 * New order
 */
public class Order {
    /**
     * @param order_id Unique autoincrement order_id
     * @param order_status Active(Unfulfilled) or Inactive(Fulfilled)
     * @param trade_type Buy or Sell order
     * @param user_id User who created order
     * @param asset_name Asset of order
     * @param quantity Quantity of Asset in order
     * @param price Value of each asset in order
     */
    public int order_id;
    public OrderStatus order_status = OrderStatus.Active;
    public OrderRequest trade_type;
    public int user_id;
    public String asset_name;
    public int quantity;
    public int price;



    /**
     * Create Order
     * @param trade_type Buy or Sell
     * @param asset_name Asset being traded
     * @param quantity Amount of asset in order
     * @param price Specified value of asset in order
     * @throws Exception Insufficient credits
     * @throws Exception User doesn't have access to requested asset (Sell only)
     */
    public Order(OrderRequest trade_type, String asset_name, int quantity, int price) throws Exception {

    }
}