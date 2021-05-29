package server.dal;

import server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrgUnit extends Connect {
    // This class holds the methods associated with the 'org_unit' table, it forms a part of the Data Access Layer.

     /* org_unit table attributes
     org_unit_id
     org_unit_name
     credits
    */

    /**
     * Constructor
     */
    public OrgUnit() {
    } // constructor, note not really required as compiler will create one automatically

    /**
     * @action Used to get all org_unit details
     * @return JSON formatted string containing all org_unit details
     */
    public String getOrgUnits() {
        String org_unitDetails = null;
        openDB();
        try {
            String sql = "select org_unit_id, org_unit_name, credits from org_unit ;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            org_unitDetails = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return org_unitDetails;
    }

    /**
     * @action Used to create an organisation
     * @param org_unit_name, credits
     * @return boolean - success or failure
     */
    public boolean createOrgUnit(String org_unit_name,int credits) {
        Boolean value = true;
        openDB();
        try {
            String sql = "insert into org_unit values (default, ?, ?)"; // default - use auto-increment id
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, org_unit_name);
            preparedStatement.setInt(2, credits);
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
     * @action Used to create an organisation
     * @param org_unit_name, credits
     * @return boolean - success or failure
     */
    public boolean updateOrgUnit(int org_unit_id, String org_unit_name,int credits) {
        Boolean value = true;
        openDB();
        try {
            String sql = "update org_unit set org_unit_name = ?, credits = ? where org_unit_id = ?;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, org_unit_name);
            preparedStatement.setInt(2, credits);
            preparedStatement.setInt(3, org_unit_id);
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
