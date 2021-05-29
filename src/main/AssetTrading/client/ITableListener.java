package client;

import common.Constants;

import java.sql.SQLException;

/**
 * Interface used to pass information back to the frame/form that set the listener
 * in this case MainFrame.
 */
public interface ITableListener {
    public void rowDeleted(Constants.TABLE_TYPE table_type, int index, int id) throws SQLException;
    public void rowSelected(Constants.TABLE_TYPE table_type, int index, int id) throws SQLException;
}
