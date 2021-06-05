package server.dal;

import server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeHistory extends Connect implements TradeHistoryDb {
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

    /**
     * Used to retrieve the latest trade history message based on organisation unit name
     *
     * @param tradeType type of trade 'BUY' or 'SELL"
     * @param orgUnitName name of organisation unit
     * @return message containing details of the latest trade
     */
    public String getLatestTradeMessage(String tradeType, String orgUnitName) {
        String latestTradeMessage = null;
        openDB();
        try {
            String sql = "select trade_type, org_unit_name, username, asset_name, quantity, price, trade_date, " +
                    "date_processed from trade_history where trade_type = ? and org_unit_name = ? " +
                    "order by date_processed desc LIMIT 1; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, tradeType);
            preparedStatement.setString(2, orgUnitName);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder sb = new StringBuilder();
            if (resultSet.next()) { // should only be 0 or 1 row
                sb.append(resultSet.getString(1) + " offer for ");
                sb.append(resultSet.getString(5) + " ");
                sb.append(resultSet.getString(4) + " was just processed [ trader: ");
                sb.append(resultSet.getString(3) + " ]");
            }
            latestTradeMessage = sb.toString();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO:

        } finally {
            closeDB();
        }

        return latestTradeMessage;
    }

}
