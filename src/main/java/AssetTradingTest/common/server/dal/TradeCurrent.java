package AssetTradingTest.common.server.dal;

import AssetTradingTest.common.server.Utilities;

import java.sql.*;

public class TradeCurrent extends Connect {
    // This class holds the methods associated with the 'trade_current' table, it forms a part of the Data Access Layer.

     /* trade table attributes
     trade_id
     trade_type
     org_unit_id
     org_unit_name
     user_id
     username
     asset_id
     asset_name
     quantity
     price
     trade_date
    */

    /**
     * Constructor
     */
    public TradeCurrent() { } // constructor, note not really required as compiler will create one automatically

    /**
     * @action Used to get all current trades
     * @param
     * @return JSON String containing the trade details or if not found null
     */
    public String getTrades() {
        String trades = null;
        openDB();
        try {
            String sql = "select trade_id, trade_type, org_unit_id, org_unit_name, user_id, username," +
                    " asset_id, asset_name, quantity, price, trade_date from trade_current;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            trades = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return trades;
    }

//        /**
//         * @action Used to create a trade
//         * @param trade_type, org_unit_id, org_unit_name, user_id, username, asset_id, asset_name, quantity, price, trade_date
//         * @return boolean - success or failure
//         */
//    public boolean createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id,
//                               String username, int asset_id, String asset_name, int quantity, int price,
//                               Timestamp trade_date) {
//        Boolean value = true;
//        openDB();
//        try {
//            String sql = "insert into trade_current values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // default - use auto-increment id
//            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
//            preparedStatement.setString(1, trade_type);
//            preparedStatement.setInt(2, org_unit_id);
//            preparedStatement.setString(3, org_unit_name);
//            preparedStatement.setInt(4, user_id);
//            preparedStatement.setString(5, username);
//            preparedStatement.setInt(6, asset_id);
//            preparedStatement.setString(7, asset_name);
//            preparedStatement.setInt(8, quantity);
//            preparedStatement.setInt(9, price);
//            preparedStatement.setTimestamp(10, trade_date);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            value = false;
//            e.printStackTrace();
//        } finally {
//            closeDB();
//        }
//
//        return value;
//    }

    /**
     * @action Used to create a trade using a stored procedure
     * @param trade_type, org_unit_id, org_unit_name, user_id, username, asset_id, asset_name, quantity, price, trade_date
     * @return boolean - success or failure
     */
    public String[] createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id,
                               String username, int asset_id, String asset_name, int quantity, int price,
                               Timestamp trade_date) {
        // Don't require org_unit_name, username, asset_name, trade_date - these are found in the procedure
        Boolean value = true;
        int runStatusNo = -1;
        String statusMsg = null;
        String[] result = {"3", "Ok"};
        int rowsUpdated = 0;
        openDB();
        try {
            CallableStatement callableStatement = this.getDBConnection().prepareCall("{ call sp_process_trade_request(?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setString(1, trade_type);
            callableStatement.setInt(2, user_id);
            callableStatement.setInt(3, org_unit_id);
            callableStatement.setInt(4, asset_id);
            callableStatement.setInt(5, quantity);
            callableStatement.setInt(6, price);
            callableStatement.registerOutParameter(7, Types.INTEGER);
            callableStatement.registerOutParameter(8, Types.NVARCHAR);
            rowsUpdated = callableStatement.executeUpdate();
            runStatusNo = callableStatement.getInt(7);
            statusMsg = callableStatement.getString(8);
            result[0] = String.valueOf(runStatusNo);
            result[1] = statusMsg;
            System.out.println("Run Status: " + runStatusNo + ", Status Msg: " + statusMsg); // TODO: debug code
        } catch (SQLException e) {
            value = false;
            result[0] = "-1";
            result[1] = e.getMessage();
            //e.printStackTrace();
        } finally {
            closeDB();
        }

        return result;
    }

    /**
     * @action Used to delete a trade using its tradeId
     * @param trade_id
     * @return boolean - success or failure
     */
    public boolean deleteTrade(int trade_id) {
        Boolean value = true;
        openDB();
        try {
            String sql = "delete from trade_current where trade_id = ?;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setInt(1, trade_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            value = false;
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return value;
    }

    public String reconcileTrades(String trade_type, String org_unit_name, String asset_name) {
        String value = "";
        boolean ok = true;
        openDB();
        try {
            // Return out of a stored procedure...
            CallableStatement callableStatement = this.getDBConnection().prepareCall("{ call sp_reconcile_trades(?, ?, ?, ?) }");
            callableStatement.setString(1, trade_type);
            callableStatement.setString(2, org_unit_name);
            callableStatement.setString(3, asset_name);
            callableStatement.registerOutParameter(4, Types.NVARCHAR);
            callableStatement.execute();
            value = callableStatement.getString(4);
        } catch (SQLException e) {
            ok = false;
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return value;
    }

}
