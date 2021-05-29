package client;

import common.Utilities;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class GenericTableRenderer implements TableCellRenderer {

    public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    public static final Color ODD_ROW_COLOR = Color.decode("#F8F8F8");
    public static final Color EVEN_ROW_COLOR = Color.WHITE;
    public static final Color FOCUSED_COLOR = Color.decode("#FDFB94");
    public static final Color SELECTED_COLOR = Color.decode("#FEFDBF");
    public static final Color BUY_COLOR = Color.decode("#28B463");
    public static final Color SELL_COLOR = Color.decode("#E74C3C");
    public static final Color VALID_TIMESTAMP_COLOR = Color.decode("#E74C3C");
    public static final Color VALID_NUMERIC_COLOR = Color.decode("#346FF6");

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Remove border from table's selected cell
        if (c instanceof JComponent) {
            ((JComponent) c).setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        // Manage colour of row selection with and without focus on the table
        if (table.getSelectedRow() == row && table.hasFocus()) {
            c.setBackground(FOCUSED_COLOR);
        } else if (isSelected) {
            table.setSelectionBackground(SELECTED_COLOR);
        } else {
            // Apply an alternating colour to the table rows
            if (row % 2 == 0) {
                c.setBackground(EVEN_ROW_COLOR);
            } else {
                c.setBackground(ODD_ROW_COLOR);
            }
        }

        String colName = table.getModel().getColumnName(column);

        if (value != null) {
            if (Utilities.isNumeric(value.toString())) {
                c.setForeground(VALID_NUMERIC_COLOR);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            }
            else if (Utilities.isValidTimestamp(value.toString())) {
                c.setForeground(VALID_TIMESTAMP_COLOR);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            }
            else if (value.equals("BUY") ) {
                ((JLabel) c).setForeground(BUY_COLOR);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            }
            else if (value.equals("SELL") ) {
                ((JLabel) c).setForeground(SELL_COLOR);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
            }
            else {
                ((JLabel) c).setHorizontalAlignment(JLabel.LEADING);
                c.setForeground(DEFAULT_FOREGROUND_COLOR); // default cell colour
            }
        }

        return c;
    }
}
