package AssetTradingTest.common.server.dal;

import AssetTradingTest.common.server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeHistory extends Connect {
    // This class holds the methods associated with the 'trade_history' table, it forms a part of the Data Access Layer.

     /* trade_history table attributes
     trade_id_sell
     trade_id_buy
     trade_type
     org_unit_name
     username
     asset_name
     quantity
     price
     trade_date
     date_processed
    */

    /**
     * Constructor
     */
    public TradeHistory() { } // constructor, note not really required as compiler will create one automatically

    /**
     * @action Used to get the trade history details
     * @param none
     * @return JSON String containing the trade history details or if not found null
     */
    public String getTradeHistory() {
        String trades = null;
        openDB();
        try {
            String sql = "select trade_id_sell, trade_id_buy, trade_type, org_unit_name, username, asset_name," +
                                                    " quantity, price, trade_date, date_processed from trade_history;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            trades = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO:

        } finally {
            closeDB();
        }

        return trades;
    }

    /**
     * @action Used to get the number of trades for a given asset_name
     * @param assetName
     * @return JSON string containing the number of trades for a given asset_name
     */
    public int getCountOfTradesForAsset(String assetName) {
        int NoOfTrades = 0;

        openDB();
        try {
            String sql = "select count(asset_name) from trade_history where UPPER(asset_name) = ?; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, assetName.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // should only be 0 or 1 row
                NoOfTrades = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO:

        } finally {
            closeDB();
        }

        return NoOfTrades;
    }

    /**
     * @action Used to get the trade history details for an asset's name
     * @param assetName
     * @return JSON string containing trade history details or if not found null
     */
    public String getTradeHistory(String assetName) {
        String tradeHistory = null;
        openDB();
        try {
            String sql = "select trade_type, org_unit_name, username, asset_name, quantity, price, trade_date," +
                                                        " date_processed from trade_history where asset_name = ?; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, assetName);
            ResultSet resultSet = preparedStatement.executeQuery();
            tradeHistory = Utilities.resultSetToJson(resultSet); // convert to common.json format
        } catch (SQLException e) {
            e.printStackTrace(); // TODO:

        } finally {
            closeDB();
        }

        return tradeHistory;
    }

}
