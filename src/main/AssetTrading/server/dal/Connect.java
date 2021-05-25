package AssetTrading.server.dal;

import AssetTrading.common.Utilities;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
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
     * @action Used to read resource file db.prop
     * @notes  database properties
     * @param none
     * @return set private properties
     */
    private void getProperties() {
        try {
            this.url = Utilities.getProperty(Connect.class, "db.prop", "jdbc.url");
            this.schema = Utilities.getProperty(Connect.class, "db.prop", "jdbc.schema");
            this.username = Utilities.getProperty(Connect.class, "db.prop", "jdbc.username");
            this.password = Utilities.getProperty(Connect.class, "db.prop", "jdbc.password");
        } catch (IOException ex) {
            //
        }
    }

    /**
     * @action Used to open MariaDB or could be a mysql database connection
     * @notes  public method to access private method
     * @param none
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
            System.out.println("openDB() SQLException " + ex.getMessage());
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("openDB() ClassNotFoundException " + ex.getMessage());
        }
        return success;
    }

    /**
     * @action Used to access MariaDB or mysql database connection
     * @notes  public method to access private method
     * @param none
     * @return mariaDb / mysql database connection
     */
    public Connection getDBConnection() {
        return this.conn;
    }

    /**
     * @action Used to close MariaDB or mysql database connection
     * @notes  public method to access private method
     * @param none
     * @return none
     */
    public void closeDB() {
        try {
            if (this.conn != null)
                this.conn.close();
        }
        catch(SQLException ex)
        {
            System.out.println("closeDB() " + ex.getMessage());
        }
    }

    /**
     * @action Used to retrieve the database schema (database name)
     * @notes  public method to access private method
     * @param none
     * @return schema name
     */
    public String getSchema() {
       return this.schema;
    }

}
