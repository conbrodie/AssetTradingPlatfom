package server;

import server.dal.TradeCurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to manage the reconciliation of Current trades
 */
public class Reconcile {

    // Variables
    protected Logger LOGGER = null;

    /**
     * Constructor
     * @param Logger
     */
    public Reconcile(Logger Logger) {
        this.LOGGER = Logger;
    }

    /**
     * Used to trigger the reconciliation of Current trades.
     * @return Returns an Object to the 'executorService' in the Server - timed from there. Object will be cast there.
     * @throws Exception
     */
    public Object call() throws Exception {
        TradeCurrent tc = new TradeCurrent();
        String[] result = tc.reconcileTrades("BUY", "Software Access Management", "CPU Hours");
        if (result.length > 0) {
            System.out.println("Reconcile stage: '" + result[0] + "', message: '" + result[1] + "'");
            if (Integer.parseInt(result[0]) < 4) {
                LOGGER.log(Level.INFO, "Reconcile stage: " + result[0] + ", message: " + result[1]);
            }
        }

        return "Called!";
    }
}
