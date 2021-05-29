package server.dal;

import server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssetHolding extends Connect {
    // This class holds the methods associated with the 'asset_holding' table, it forms a part of the Data Access Layer.

     /* asset_holding table attributes
     org_unit_id
     asset_id
     quantity
    */

    /**
     * Constructor
     */
    public AssetHolding() { } // constructor, note not really required as compiler will create one automatically

    /**
     * @action Used to get all asset_holdings
     * @return JSON formatted string containing the all details of the asset_holdings table
     */
    public String getAssetHoldings() {
        String assetHoldings = null;
        openDB();
        try {
            String sql = "select org_unit_id, asset_id, quantity from asset_holding ;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            assetHoldings = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return assetHoldings;
    }

    /**
     * @action Used to create an asset holding
     * @param org_unit_id, asset_id, quantity
     * @return boolean - success or failure
     */
    public boolean createAssetHolding(int org_unit_id, int asset_id, int quantity) {
        Boolean value = true;
        openDB();
        try {
            String sql = "insert into asset_holding values (?, ?, ?);";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setInt(1, org_unit_id);
            preparedStatement.setInt(2, asset_id);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            value = false;
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return value;
    }

    /**
     * @action Used to update an asset holding
     * @param org_unit_id, asset_id, quantity
     * @return boolean - success or failure
     */
    public boolean updateAssetHolding(int org_unit_id, int asset_id, int quantity) {
        Boolean value = true;
        openDB();
        try {
            String sql = "update asset_holding set quantity = ? where org_unit_id = ? and asset_id = ?;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, org_unit_id);
            preparedStatement.setInt(3, asset_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            value = false;
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return value;
    }
}
