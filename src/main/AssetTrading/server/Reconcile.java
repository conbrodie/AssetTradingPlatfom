package AssetTrading.server;

import AssetTrading.server.dal.TradeCurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        // TODO: work out how to reconcile current trades using procedure
        TradeCurrent tc = new TradeCurrent();
        String trades = tc.reconcileTrades("SELL", "Computer Cluster Division", "CPU Hours");
        System.out.println("Reconcile message: " + trades);
        LOGGER.log(Level.INFO, "Table has been processed!");

        return "Called!";
    }
}
