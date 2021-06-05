package server.dal;

import server.Utilities;

import java.sql.*;

public class TradeCurrent extends Connect implements TradeCurrentDb {
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

    /**
     * @action Used to create a trade using a stored procedure
     * @param trade_type, org_unit_id, org_unit_name, user_id, username, asset_id, asset_name, quantity, price, trade_date
     * @return String[] - contains a run status number, status message and the trade_id of the new trade
     */
    public String[] createTrade(String trade_type, int org_unit_id, String org_unit_name, int user_id,
                               String username, int asset_id, String asset_name, int quantity, int price,
                               Timestamp trade_date) {
        // Don't require org_unit_name, username, asset_name, trade_date - these are found in the procedure
        int runStatusNo = -1;
        String statusMsg = null;
        int statusPK = -1;
        String[] result = { "3", "Ok", "-1" };
        int rowsUpdated = 0;
        openDB();
        try {
            CallableStatement callableStatement = this.getDBConnection().prepareCall("{ call sp_process_trade_request(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setString(1, trade_type);
            callableStatement.setInt(2, user_id);
            callableStatement.setInt(3, org_unit_id);
            callableStatement.setInt(4, asset_id);
            callableStatement.setInt(5, quantity);
            callableStatement.setInt(6, price);
            callableStatement.registerOutParameter(7, Types.INTEGER);
            callableStatement.registerOutParameter(8, Types.NVARCHAR);
            callableStatement.registerOutParameter(9, Types.NVARCHAR);
            rowsUpdated = callableStatement.executeUpdate();
            runStatusNo = callableStatement.getInt(7);
            statusMsg = callableStatement.getString(8);
            statusPK = callableStatement.getInt(9);
            result[0] = String.valueOf(runStatusNo); // stage in the proc it got to
            result[1] = statusMsg; // message
            result[2] = String.valueOf(statusPK); // last inserted id
            System.out.println("createTrade status: " + runStatusNo + ", Status Msg: " + statusMsg); // TODO: debug code
        } catch (SQLException e) {
            result[0] = "-1";
            result[1] = e.getMessage();
            result[2] = "-1";
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

    public String[] reconcileTrades(String trade_type, String org_unit_name, String asset_name) {
        boolean procedureStatus = false;
        boolean ok = true;

        int runStatusNo = -1;
        String statusMsg = null;
        String[] result = { "3", "Ok" };

        openDB();
        try {
            // Return out of a stored procedure...
            CallableStatement callableStatement = this.getDBConnection().prepareCall("{ call sp_process_trades(?, ?) }");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.registerOutParameter(2, Types.NVARCHAR);
            procedureStatus = callableStatement.execute();
            runStatusNo = callableStatement.getInt(1);
            statusMsg = callableStatement.getString(2);
            result[0] = String.valueOf(runStatusNo); // stage in the proc it got to
            result[1] = statusMsg; // message
        } catch (SQLException e) {
            result[0] = "-1";
            result[1] = e.getMessage();
        } finally {
            closeDB();
        }

        return result;
    }

}
