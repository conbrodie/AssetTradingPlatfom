package AssetTradingPlatform;

import java.util.TreeSet;

/**
 * A class to manage active buy/sell listings and their properties (price, amount, ...)
 */
public class Market {
    /**
     * @param activeMarketPlace Holds all Active(Unfulfilled) orders
     * @param historicalMarketPlace Holds Inactive(Fulfilled) orders in order of time placed (most recent to least recent). Must only contain orders within a range (Last 7 days of orders)
     */
    public TreeSet activeMarketPlace;
    public TreeSet historicalMarketPlace;


    /**
     * A class to create a new market place (a collection, list or db to hold orders)
     */
    public Market(){

    }


    /**
     * Takes an order and adds it to market using its orderID
     * @param orderID An order object
     * @see #orderCheck() Used when adding an order
     * @see #orderMatch() Used when adding an order
     */
    public void AddOrder(int orderID){

    }


    /**
     * Remove a listed order
     * @param orderID Unique order ID
     */
    public void RemoveOrder(int orderID){

    }


    /**
     * Relist an order
     * @param orderID Unique order ID
     * @see #RemoveOrder(int orderID) Called for same orderID in method
     * @see #AddOrder(int orderID) Called for same orderID in method
     */
    public void RelistOrder(int orderID){

    }


    /**
     * Checks if incoming order has sufficient resources are available to place order
     */
    public void orderCheck(){

    }


    /**
     * Checks if incoming order could be fulfilled with other currently active orders
     */
    public void orderMatch(){

    }
}
