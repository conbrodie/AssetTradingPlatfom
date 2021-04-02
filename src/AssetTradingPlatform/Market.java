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
     * A class to create a new market place (a collection, list or db to hold requested trades)
     */
    public Market(){

    }

    /**
     * Takes an order and adds it to class
     * @param Order An order object
     * @see orderCheck
     * @see orderMatch
     */
    public void AddOrder(){

    }

    /**
     * Remove a listed order
     * @param id Unique order ID
     */
    public void RemoveOrder(){

    }

    /**
     * Relist an order
     * @param id Unique order ID
     * @see removeOrder
     * @see addOrder
     */
    public void RelistOrder(){

    }

    /**
     * Checks an incoming order/trade request and checks the required resources are avaliable to place order
     */
    public void orderCheck(){

    }

    /**
     * Checks an incoming order/trade request and checks if any other currently active orders could fulfil request
     */
    public void orderMatch(){

    }





}
