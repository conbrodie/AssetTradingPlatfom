package AssetTradingPlatform.client.gui.model;

import AssetTradingPlatform.common.Unit;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;

public class UnitTableModel extends AbstractTableModel {
    private List<Unit> units;
    private static String[] columns = new String[]{"Org Unit", "Total Assets", "Total Credits", ""};
    public UnitTableModel(List<Unit> units) {
        this.units = units;
    }

    public UnitTableModel() {
        this(Collections.emptyList());
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return units.size();
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
        Unit unit  = units.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return unit.getOrgUnitName();
            case 1:
                return unit.getOrgAssets().size();
            case 2:
                return unit.getCredits();
            case 3:
                return unit.getOrgUnitId();
        }
        return null;
    }
}
