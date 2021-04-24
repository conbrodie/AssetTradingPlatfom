package AssetTradingPlatform.common;

import java.util.Date;
import java.util.TreeMap;

/**
 * Manages active orders including: fulfilling orders by matching them with others, moving fulfilled orders from active to historical
 */
public class Market {
    /**
     * Time which order was listed on market
     */
    Date date_listed;
    /**
     * Time which order was fulfilled
     */
    Date date_fulfilled = null;

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
     * Takes an order and adds it to trade_current via its orderID
     * @param orderID An order object
     * @see #OrderCheck(int orderID) Called when adding an order
     * @see #OrderMatch(int orderID) Called when adding an order
     */
    public void AddOrder(int orderID){
        date_listed = new Date();
    }


    /**
     * Remove an active listed order from trade_current via its orderID
     * @param orderID Unique order ID
     */
    public void RemoveOrder(int orderID){

    }


    /**
     * Relist an active order to trade_current via its orderID
     * @param orderID Unique order ID
     * @see #RemoveOrder(int orderID)
     * @see #AddOrder(int orderID)
     */
    public void RelistOrder(int orderID){

    }


    /**
     * Checks if incoming order has sufficient resources available to place order
     * @param orderID Unique order ID
     */
    public void OrderCheck(int orderID){

    }


    /**
     * Checks if incoming order could be fulfilled by other currently active orders.
     * @param orderID Unique order ID
     */
    public void OrderMatch(int orderID){

    }

    /**
     * Used to move order from active to historical. Called when order has been filled.
     * Should create and pass a date_fulfilled of the order, the date_listed, and the orderID.
     * @see #RemoveOrder(int orderID)
     * @param orderID Unique order ID
     */
    public void OrderTransfer(int orderID){

    }
}
