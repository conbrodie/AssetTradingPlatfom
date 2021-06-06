package server.dal;

import java.sql.Timestamp;

public class TradeCurrentDbMock implements TradeCurrentDb {

    private String getTradesReturn;
    private String[] createTradesReturn;
    private Boolean deleteTradeReturn;
    private String[] reconcileTradesReturn;

    public TradeCurrentDbMock(String getTradesReturn, String[] createTradesReturn, Boolean deleteTradeReturn, String[] reconcileTradesReturn) {
        this.getTradesReturn = getTradesReturn;
        this.createTradesReturn = createTradesReturn;
        this.deleteTradeReturn = deleteTradeReturn;
        this.reconcileTradesReturn = reconcileTradesReturn;
    }

    @Override
    public String getTrades() {
        return getTradesReturn;
    }

    @Override
    public String[] createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id, String username, int asset_id, String asset_name, int quantity, int price, Timestamp trade_date) {
        return createTradesReturn;
    }

    @Override
    public boolean deleteTrade(int trade_id) {
        return deleteTradeReturn;
    }

    @Override
    public String[] reconcileTrades(String trade_type, String org_unit_name, String asset_name) {
        return reconcileTradesReturn;
    }
}
