package AssetTradingPlatform.client.gui.model;

import AssetTradingPlatform.common.Order;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;

public class OrgTradeTableModel extends AbstractTableModel {
    private List<Order> orders;
    private static String[] columns = new String[]{"Status", "Org Unit", "Asset Name", "Quantity", "Price", ""};

    public OrgTradeTableModel(List<Order> orders) {
        this.orders = orders;
    }

    public OrgTradeTableModel() {
        this(Collections.emptyList());
    }

    public void setUnits(List<Order> orders) {
        this.orders = orders;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return orders.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order  = orders.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.getTrade_type();
            case 1:
                return order.getUnit_id();
            case 2:
                return order.getAsset_name();
            case 3:
                return order.getQuantity();
            case 4:
                return order.getPrice();
        }
        return null;
    }
}
