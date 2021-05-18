package AssetTradingPlatform.common;

import AssetTradingPlatform.common.OrderRequest;

import java.time.LocalDateTime;

/**
 * New order
 */
public class Order {
    /**
     * Unique autoincrement order_id
     */
    public int order_id;
    /**
     * Active(Unfulfilled) or Inactive(Fulfilled)
     */
    public OrderStatus order_status = OrderStatus.Active;
    /**
     * Buy or Sell order
     */
    public OrderRequest trade_type;
    /**
     * User who created order
     */
    public int user_id;
    /**
     * Unit related to user
     */
    public int unit_id;
    /**
     * Asset of order
     */
    public String asset_name;
    /**
     * Quantity of Asset in order
     */
    public int quantity;
    /**
     * Value of each asset in order
     */
    public int price;


    /**
     * Create Order
     *
     * @param trade_type Buy or Sell
     * @param asset_name Asset being traded
     * @param quantity   Amount of asset in order
     * @param price      Specified value of asset in order
     * @throws Exception Insufficient credits
     * @throws Exception User doesn't have access to requested asset (Sell only)
     */
    public Order(OrderRequest trade_type, String asset_name, int quantity, int price) throws Exception {

    }

    public int getOrder_id() {
        return order_id;
    }

    public OrderStatus getOrder_status() {
        return order_status;
    }

    public OrderRequest getTrade_type() {
        return trade_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
