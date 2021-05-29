package client;

import common.models.TradeCurrentModel;

import javax.sql.RowSetEvent;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Custom Table Model derived from the AbstractTableModel. This allow better implementation of the JTable's
 * features.
 */

public class TradeTableModel extends AbstractTableModel implements TableModelListener {

    private ArrayList<TradeCurrentModel> trades;

    // Define the column 'types' for the table
    private final Class[] columnClass = new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class };

    // Define the column 'names' for the table
    private final String[] colNames = { "Type", "Organisation", "Trader", "Asset Name", "Qty", "Price", "Date" };

    // Constructor
    public TradeTableModel(ArrayList<TradeCurrentModel> currentTrades) {
        this.addTableModelListener(this);
        this.trades = currentTrades; // set reference to master ArrayList of TradeCurrent in MainFrame - contains data from server
    }

    @Override
    public int findColumn(String columnName) {
        return Arrays.asList(colNames).indexOf(columnName);
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    @Override
    public int getRowCount() {
        return trades.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            default:
                return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (trades != null) {
            TradeCurrentModel trade = trades.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    trade.setTrade_type((String) aValue);
                case 1:
                    trade.setOrg_unit_name((String) aValue);
                case 2:
                    trade.setUsername((String) aValue);
                case 3:
                    trade.setAsset_name((String) aValue);
                case 4:
                    trade.setQuantity((int) aValue);
                case 5:
                    trade.setPrice((int) aValue);
                case 6:
                    trade.setTrade_date((Timestamp) aValue);
                case 7:
                    trade.setTrade_id((int) aValue); // needed to get access to trade_id
            }
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

//    // This must be commented out due to 'Cast Integer to String Error' when using JTable sorter...
//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//        return columnClass[columnIndex];
//    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // for display in table - no milli-secs
        if (trades != null) {
            TradeCurrentModel trade = trades.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return trade.getTrade_type();
                case 1:
                    return trade.getOrg_unit_name();
                case 2:
                    return trade.getUsername();
                case 3:
                    return trade.getAsset_name();
                case 4:
                    return trade.getQuantity();
                case 5:
                    return trade.getPrice();
                case 6:
                    return df.format(trade.getTrade_date());
                case 7:
                    return trade.getTrade_id(); // needed to get access to trade_id
            }
        }
        return null;
    }

    public void refreshTrades(ArrayList<TradeCurrentModel> currentTrades) {
        trades.clear();
        trades = currentTrades;
        fireTableDataChanged();
    }

    public void addTrade(TradeCurrentModel trade) {
        int rowCount = getRowCount();
        this.trades.add(trade);
        this.fireTableRowsInserted(rowCount, rowCount);
    }

    public void removeTrade(TradeCurrentModel trade) {
        if (trades.contains(trade)) {
            int index = trades.indexOf(trade);
            trades.remove(trade);
            fireTableRowsDeleted(index, index);
        }
    }

    public void removeTrade(int trade_id) {
        for (int index = 0; index < trades.size(); index++ ) {
            TradeCurrentModel tcm = trades.get(index);
            if (tcm.getTrade_id() == trade_id) {
                trades.remove(tcm);
                fireTableRowsDeleted(index, index);
                break;
            }
        }
    }

    public void removeTradeAt(int row) {
        this.trades.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
//            System.out.println("type@: " + e.getType());
//            if(e.getType() == TableModelEvent.INSERT) {
//                System.out.println("^^TableModelEvent.INSERT");
//            }
//            if(e.getType() == TableModelEvent.UPDATE) {
//                System.out.println("^^TableModelEvent.UPDATE");
//            }
//            if(e.getType() == TableModelEvent.DELETE) {
//                System.out.println("^^TableModelEvent.DELETE");
//            }
//            TableModel model = (TableModel)e.getSource();
//            System.out.println("table changed: " + model);
//            String columnName = model.getColumnName(column);
//            Object data = model.getValueAt(row, column);

            // Do something with the data... // TODO: to be removed - not used...
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
        //System.out.println("Table Data Changed has changed, row count:" + trades.size()); // TODO: debug code
    }

    @Override
    public void fireTableStructureChanged() {
        System.out.println("Table structure has changed!"); // TODO: debug code
    }
}