package server.dal;

import server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountType extends Connect implements AccountTypeDb {
    // This class holds the methods associated with the 'account_type' table, it forms a part of the Data Access Layer.

     /* account_type table attributes
     account_type_id
     account_type
    */

    /**
     * Constructor
     */
    public AccountType() { }

    /**
     * @action Used to get all account type details
     * @return JSON formatted string containing the all details of the account_type table
     */
    public String getAccounts() {
        String accountTypeDetails = null;
        openDB();
        try {
            String sql = "select account_type_id, account_type from account_type;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            accountTypeDetails = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return accountTypeDetails;
    }

}
