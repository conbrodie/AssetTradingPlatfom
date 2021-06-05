package server.dal;

import server.Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Connect {
    // This class holds the methods associated with the 'user' table, it forms a part of the Data Access Layer.

    /* User table attributes
     user_id
     username
     password (hashed)
     account_type_id
     org_unit_id
    */

    /**
     * Constructor
     */
    public User() { } // constructor, note not really required as compiler will create one automatically

    /**
     * @action Used to get the user's credentials
     * @param username A unique username
     * @return JSON formatted string containing the user's hashed password or if not found null
     */
    public String getUserSecurityCredentials(String username) {
        String credentials = null;
        openDB();
        try {
            String sql = "select password from user where username = ? ; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // should only be 0 or 1 row
                credentials = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return credentials;
    }

    /**
     * @action Used to get the user's id
     * @param username A unique username
     * @return user_id or 0 if username not in table
     */
    public int getUserId(String username) {
        int id = 0;
        openDB();
        try {
            String sql = "select user_id from user where username = ? ; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // should only be 0 or 1 row
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return id;
    }

    /**
     * @action Used to get all user details
     * @return JSON formatted string containing all user_id and associated names
     */
    public String getUsers() {
        String users = null;
        openDB();
        try {
            String sql = "select user_id, username, password, org_unit_id, account_type_id from user;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            users = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return users;
    }

    /**
     * Used to get the user's extended details - org_unit_name and account_type
     * @param username A unique username
     * @return JSON formatted string containing the user's details plus account name and org unit name or if not found null
     */
    public String getUserDetails(String username) {
        String userDetails = null;
        openDB();
        try {
            String sql = "select user_id, username, u.org_unit_id, ou.org_unit_name," +
                    " u.account_type_id, at.account_type from ((user u inner join" +
                    " account_type at ON u.account_type_id = at.account_type_id) inner join" +
                    " org_unit ou on u.org_unit_id = ou.org_unit_id) where username = ?; ";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // should only be 0 or 1 row
                userDetails = Utilities.resultSetToJson(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return userDetails;
    }

    /**
     * @action Used to get the selected user details - just user_id and username
     * @return JSON formatted string containing all user_id and associated usernames
     */
    public String getUsernames() {
        String users = null;
        openDB();
        try {
            String sql = "select user_id, username from user;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            users = Utilities.resultSetToJson(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeDB();
        }

        return users;
    }

    /**
     * @action Used to create a user
     * @param username, password, org_unit_id, account_type_id
     * @return boolean - success or failure
     */
    public boolean createUser(String username, String password, int org_unit_id, int account_type_id) {
        Boolean value = true;
        openDB();
        try {
            String sql = "insert into user (user_id, username, password, org_unit_id, account_type_id) values (default, ?, ?, ?, ?)"; // default - use auto-increment id
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, org_unit_id);
            preparedStatement.setInt(4, account_type_id);
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
     * @action Used to update a user
     * @param user_id, username, password, org_unit_id, account_type_id
     * @return boolean - success or failure
     */
    public boolean updateUser(int user_id, String username, String password, int org_unit_id, int account_type_id) {
        Boolean value = true;
        openDB();
        try {
            String sql = "update user set username = ?, password = ?, org_unit_id = ?, account_type_id = ? where user_id = ?;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, org_unit_id);
            preparedStatement.setInt(4, account_type_id);
            preparedStatement.setInt(5, user_id);
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
     * @action Used to update a user's password, will check that existing password is as expected
     * @param username - A unique username, new password hashed format, old password hashed format
     * @return boolean - success or failure
     */
    public boolean changePassword(String username, String newPassword, String oldPassword) throws SQLException {
        Boolean value = true;
        openDB();
        try {
            String password = null;
            String sql = "select password from user where username = ?;";
            PreparedStatement preparedStatement = this.getDBConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // should only be 0 or 1 row
                password = resultSet.getString("password");
            }
            // First check if old password equals passed old password - just a check
            if (oldPassword.equals(password)) {
                sql = "update user set password = ? where username = ?;";
                preparedStatement.clearParameters();
                preparedStatement = this.getDBConnection().prepareStatement(sql);
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
            else {
                value = false;
            }
        } catch (SQLException e) {
            value = false;
            e.printStackTrace();
        } finally {
            closeDB();
        }

        return value;
    }

}
