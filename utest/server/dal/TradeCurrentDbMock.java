package server.dal;

import java.sql.Timestamp;

public class TradeCurrentDbMock  implements TradeCurrentDb {



    @Override
    public String getTrades() {
        return null;
    }

    @Override
    public String[] createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id, String username, int asset_id, String asset_name, int quantity, int price, Timestamp trade_date) {
        return new String[0];
    }

    @Override
    public boolean deleteTrade(int trade_id) {
        return false;
    }

    @Override
    public String[] reconcileTrades(String trade_type, String org_unit_name, String asset_name) {
        return new String[0];
    }
}
