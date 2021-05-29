package server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utilities {

    // https://stackoverflow.com/questions/18960446/how-to-convert-a-java-resultset-into-json
    // Minor changes to formatting, variable name and method name to better reflect what it does.

    /**
     * This static class is used to convert a jdbc ResultSet to a json structured string.
     *
     * @param rs jdbc ResultSet
     * @return json formatted string
     * @throws SQLException
     */

    public static String resultSetToJson(ResultSet rs) throws SQLException
    {
        // Check for empty recordset
        if (rs.first() == false) {
            return "[]";
        }
        else {
            rs.beforeFirst();
        }

        StringBuilder sb = new StringBuilder();
        Object item;
        String value;
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();

        sb.append("[{");
        while (rs.next()) {
            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnName(i);
                item = rs.getObject(i);
                if (item != null ) {
                    value = item.toString();
                    value = value.replace('"', '\'');
                }
                else {
                    value = "null";
                }
                sb.append("\"" + column_name + "\":\"" + value +"\",");

            } //end For = end record
            sb.setCharAt(sb.length() - 1, '}'); // replace last comma with curly bracket
            sb.append(",{");
        }  // end While = end resultset

        sb.delete(sb.length() - 3, sb.length()); // delete last two chars
        sb.append("}]");

        return sb.toString();
    }
}
