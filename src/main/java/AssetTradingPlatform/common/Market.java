package AssetTradingPlatform.common;

import java.util.Date;
import java.util.TreeMap;

/**
 * Manages trades by matching orders
 */
public class Market {
    /**
     * Unique ID of a trade
     */
    int trade_id;
    /**
     * Time which order was listed on market
     */
    Date date_listed;
    /**
     * Time which order was fulfilled
     */
    Date date_fulfilled = null;
    /**
     * ID of order being filled
     */
    int trade_buy;
    /**
     * ID of order filling the original order
     */
    int trade_sell;

    /**
     * Define market place which consists of two sets/tables to track active and non-active orders.
     */
    public Market(){
        /**
         * TEMPORARY set (before connected to DB) to hold Active (Unfulfilled) orders
         */
        TreeMap trade_current = new TreeMap();
        /**
         * TEMPORARY set (before connected to DB) to hold Inactive(Fulfilled) orders
         */
        TreeMap trade_history = new TreeMap();
    }


    /**
     * Takes an order and adds it to trade_current via its order_id
     * @param order_id An order object
     * @see #OrderCheck(int order_id) Called when adding an order
     * @see #OrderMatch(int order_id) Called when adding an order
     */
    public void AddOrder(int order_id){
        date_listed = new Date();
    }


    /**
     * Remove an active listed order from trade_current via its order_id
     * @param order_id Unique order ID
     */
    public void RemoveOrder(int order_id){

    }


    /**
     * Relist an active order to trade_current via its order_id
     * @param order_id Unique order_id ID
     * @see #RemoveOrder(int order_id)
     * @see #AddOrder(int order_id)
     */
    public void RelistOrder(int order_id){

    }


    /**
     * Checks if incoming order has sufficient resources available to place order
     * @param order_id Unique order_id
     */
    public void OrderCheck(int order_id){

    }


    /**
     * Checks if incoming order could be fulfilled by other currently active orders.
     * @param order_id Unique order_id
     */
    public void OrderMatch(int order_id){

    }

    /**
     * Used to move order from active to historical. Called when order has been filled.
     * Should create and pass a date_fulfilled of the order, the date_listed, and the order_id.
     * Should update orders status.
     * @see #RemoveOrder(int order_id)
     * @param order_id Unique order_id
     */
    public void OrderTransfer(int order_id){

    }
}
