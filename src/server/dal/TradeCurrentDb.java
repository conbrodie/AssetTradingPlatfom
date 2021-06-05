package server.dal;

import java.sql.Timestamp;

public interface TradeCurrentDb {
    String getTrades();
    String[] createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id,
                         String username, int asset_id, String asset_name, int quantity, int price,
                         Timestamp trade_date);
    boolean deleteTrade(int trade_id);
    String[] reconcileTrades(String trade_type, String org_unit_name, String asset_name);

}
