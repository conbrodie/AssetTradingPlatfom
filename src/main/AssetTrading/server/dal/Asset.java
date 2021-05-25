package AssetTrading.server.dal;

import AssetTrading.server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Asset extends Connect {
    // This class holds the methods associated with the 'asset' table, it forms a part of the Data Access Layer.

     /* asset table attributes
     asset_id
     asset_name
    */

    /**
     * Constructor
     */
    public Asset() { }

    /**
     * @action Used to get all assets
     * @param none
     * @return JSON formatted string containing the all details of the asset table
     */
    public String getAssets() {
        String assets = null;
        openDB();
        try {
            String sql = "select asset_id, asset_name from asset;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            assets = Utilities.resultSetToJson(resultSet);;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return assets;
    }

    /**
     * @action Used to create an asset
     * @param asset_name
     * @return boolean - success or failure
     */
    public boolean createAsset(int asset_id, String asset_name) {
        Boolean value = true;
        openDB();
        try {
            String sql = "insert into asset values (default, ?)";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, asset_name);
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
