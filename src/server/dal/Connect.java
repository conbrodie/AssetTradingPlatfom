package server.dal;

import common.Utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection class - used to allow other classes to inherit methods and properties
 * Retrieves properties from resource files - see files under resources directory
 */

public class Connect {
    private String url;
    private String schema;
    private String username;
    private String password;
    private Connection conn = null;

    /**
     * Used to get properties on creation of object
     */
    public Connect() {
        this.getProperties();
    }

    /**
     * Used to read resource file db.prop. Contains database properties.
     */
    private void getProperties() {
        try {
            this.url = Utilities.getProperty(Connect.class, "db.prop", "jdbc.url");
            this.schema = Utilities.getProperty(Connect.class, "db.prop", "jdbc.schema");
            this.username = Utilities.getProperty(Connect.class, "db.prop", "jdbc.username");
            this.password = Utilities.getProperty(Connect.class, "db.prop", "jdbc.password");
        } catch (IOException ex) {
            System.out.println("Properties could not be retrieved!"); // TODO:
        }
    }

    /**
     * Used to open MariaDB or could be a mysql database connection
     * @return boolean - success or failure
     */
    public Boolean openDB() {
        Boolean success = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = (Connection) DriverManager.getConnection(this.url + "/" + this.schema + "?useSSL=false&" + "user=" + this.username + "&password=" + this.password);
            success = true;
        }
        catch(SQLException ex)
        {
            System.out.println("openDB() SQLException " + ex.getMessage()); // TODO:
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("openDB() ClassNotFoundException " + ex.getMessage()); // TODO:
        }
        return success;
    }

    /**
     * Used to close MariaDB or mysql database connection
     * @return DB Connection
     */
    public Connection getDBConnection() {
        return this.conn;
    }

    /**
     * Used to close MariaDB or mysql database connection
     */
    public void closeDB() {
        try {
            if (this.conn != null)
                this.conn.close();
        }
        catch(SQLException ex)
        {
            System.out.println("closeDB() " + ex.getMessage()); // TODO:
        }
    }

    /**
     * Used to retrieve the database schema (database name)
     * @return schema name
     */
    public String getSchema() {
       return this.schema;
    }

}
