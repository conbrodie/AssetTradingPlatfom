package client;

import javax.swing.table.AbstractTableModel;

/**
 * Used to provide an empty table display to the user on first opening - before logging in
 */
public class EmptyTableModel extends AbstractTableModel {

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return "Login to view current trading information";
    } // message to user

}
