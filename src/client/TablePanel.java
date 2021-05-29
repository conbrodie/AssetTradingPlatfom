package client;

import common.Constants;
import common.models.TradeCurrentModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablePanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(TablePanel.class.getName());

    private JTable table;
    private JPopupMenu popup;
    private ITableListener tableListener;
    private Constants.TABLE_TYPE table_type;
    private MessagePanel messagePanel;
    private StatusPanel statusPanel;

    public TablePanel() {

        table = new JTable(new EmptyTableModel());
        table_type = Constants.TABLE_TYPE.empty;
        GenericTableRenderer tableRenderer = new GenericTableRenderer();
        table.setDefaultRenderer(Object.class, tableRenderer);
        table.setAutoCreateRowSorter(false); // turn on/off automatic column sorting
        this.setLayout(new BorderLayout());

        messagePanel = new MessagePanel();
        this.add(messagePanel, BorderLayout.NORTH); // will create a JTable...

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //table.setFillsViewportHeight(true); // not required here

        this.add(scrollPane,  BorderLayout.CENTER) ;

        statusPanel = new StatusPanel();
        this.add(statusPanel, BorderLayout.SOUTH);

        // Add the pop-up menu to the table
        popup = new JPopupMenu();
        JMenuItem miRemoveItem = new JMenuItem("Delete row");
        popup.add(miRemoveItem);

        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Lost Focus Listener event");
            }
            @Override
            public void focusGained(FocusEvent e) {
                // Manage table's row selections when focus is gained. If row selection or no selection
                if (table.getModel() instanceof EmptyTableModel) { return; }

                // Could have filter ON - so need to translate selected row to model data

                if (table.getSelectionModel().isSelectionEmpty()) { // no rows selected
                    if (table.getRowCount() > 0) { // table has rows, so select the first one
                        table.changeSelection(0, 0, false, false); // select it...
                        // Could have filter ON - so need to translate selected row to model data
                        int rowTranslated = table.convertRowIndexToModel(table.getSelectedRow());
                        // Get first row's contents - the model has been set up to have the last column as the TradeId
                        Object tradeId = table.getModel().getValueAt(rowTranslated, table.getModel().getColumnCount());
                        try {
                            tableListener.rowSelected(table_type, 0, (int) tradeId);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
                else { // there is a row selected...
                    int row = table.getSelectionModel().getMaxSelectionIndex();
                    int rowTranslated = table.convertRowIndexToModel(table.getSelectedRow());
                    Object tradeId = table.getModel().getValueAt(rowTranslated, table.getModel().getColumnCount());
                    table.changeSelection(row, 0, false, false); // use filtered table row
                    try {
                        tableListener.rowSelected(table_type, row, (int) tradeId);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int rowSelected = table.rowAtPoint(p);

                table.getSelectionModel().setSelectionInterval(rowSelected, rowSelected); // needed to select row!!
                System.out.println("Mouse Listener - table row selected " + rowSelected);

                if (e.getButton() == MouseEvent.BUTTON1 && rowSelected >= 0) {
                    try {
                        // Could have filter ON - so need to translate selected row to model data
                        int rowTranslated = table.convertRowIndexToModel(table.getSelectedRow());
                        Object tradeId = table.getModel().getValueAt(rowTranslated, table.getModel().getColumnCount());
                        System.out.println("Selected - Row Index: " + rowSelected + ", TradeId: " + tradeId);
                        tableListener.rowSelected(table_type, rowSelected, (int) tradeId); // send to MainFrame event handler
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "SQL Error " + ex.getMessage());
                    }
                    catch(NullPointerException ex) {
                        System.out.println("**** NullPointerException ****"); // TODO: debug code
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3 && rowSelected >= 0 ) {
                    // Add table types to make deletion unavailable
                    if (table_type != Constants.TABLE_TYPE.empty) {
                        popup.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        miRemoveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableListener != null) {
                    int rowSelected = table.getSelectedRow(); // as viewed in table

                    // Could have filter ON - so need to translate selected row to model data
                    System.out.println("$selected index: " + table.getSelectedRow());
                    int rowTranslated = table.convertRowIndexToModel(table.getSelectedRow());
                    // Let user confirm deletion ow this row
                    int colIndex = ((TradeTableModel) table.getModel()).findColumn("Organisation");
                    Object orgUnit = table.getModel().getValueAt(rowTranslated, colIndex);
                    if(! statusPanel.getOrgUnit().equals(orgUnit)) {
                        JOptionPane.showMessageDialog(getParent(),
                                "You can only delete trades from your own organisation!",
                                "Delete Trade Message", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    // Now start the delete process
                    if (JOptionPane.showConfirmDialog(TablePanel.this, "Are you sure?",
                            "Delete Row Conformation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try {
                            System.out.println("miRemoveItem - rowNo: " + rowSelected); // TODO: debug code
                            Object tradeId = table.getModel().getValueAt(rowTranslated, table.getModel().getColumnCount());
                            System.out.println("Delete - Row Index: " + rowSelected + ", TradeId: " + tradeId + ", OrgUnit: " + orgUnit); // TODO: debug code
                            tableListener.rowDeleted(table_type, rowSelected, (int) tradeId); // use listener to send info out
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "SQL Error " + ex);
                            JOptionPane.showMessageDialog(TablePanel.this,
                                    "Could not complete deletion!", "Delete Row Error",
                                    JOptionPane.OK_OPTION);
                        }
                        switch (table_type) {
                            case trade:
                                //((TradeTableModel) table.getModel()).fireTableRowsDeleted(rowTranslated, rowTranslated);
                                break;
                            case empty: break;
                            default:
                                LOGGER.log(Level.SEVERE, "Table type " + table_type + " is unknown.");
                                LOGGER.log(Level.INFO, "Table model is " + table.getModel());
                                return;
                        }
                    }
                    else {
                        System.out.println("else statement row =  " + rowSelected); // TODO: debug code
                        table.getSelectionModel().setSelectionInterval(rowSelected, rowSelected);
                        table.changeSelection(rowSelected, 0, false, false);
                    }
                }
            }
        });

    }

    public JTable getTable() {
        return this.table;
    }

    public JPanel getMessagePanel() {
        return this.messagePanel;
    }

    public JPanel getStatusPanel() {
        return this.statusPanel;
    }

    public void setParameters(int tablePreferredWidth, double... percentages) {
        setJTableColumnsWidth(table, tablePreferredWidth, percentages); // set column widths
    }
    public void setTableType() {
        table_type = Constants.TABLE_TYPE.trade; // added as of 26/4
    }

    // Current Trades data...
    public void setTradeData(List<TradeCurrentModel> trades) {
        table_type = Constants.TABLE_TYPE.trade;
        table.requestFocus();
        table.requestFocusInWindow();
    }
    public void refreshTradeData() {

        ((TradeTableModel) table.getModel()).fireTableDataChanged();
        System.out.println("... ask table to fire table changed event!"); // TODO: debug code
    }

    private void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
        // Adjust column widths of a table based on preferred width and percentages
        // Because of multiple table layouts must use 'fireTableStructureChanged()' event

        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)(tablePreferredWidth * (percentages[i] / total)));
        }
    }

    public void setTableListener(ITableListener listener) {
        this.tableListener = listener;
    }
}