package AssetTradingPlatform.client.gui.panels;

import AssetTradingPlatform.client.gui.Gbc;
import AssetTradingPlatform.client.gui.GuiColours;
import AssetTradingPlatform.client.gui.model.UnitTableModel;
import AssetTradingPlatform.common.Unit;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import static AssetTradingPlatform.client.gui.Gbc.addToPanel;

public class OrganisationUnits extends JPanel {
    private EventListenerList listenerList = new EventListenerList();
    private JLabel lblTitle = new JLabel("Organisation Units");
    private UnitTableModel model  = new UnitTableModel();
    private JTable tblUnits = new JTable(model);
    private JPanel units = new JPanel();

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton component = new JButton("View");
        private int unitId;
        public ButtonEditor() {
            component.addActionListener((event) -> {
                fireActionPerformed(unitId);
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            unitId = (Integer)value;
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            return unitId;
        }
    }

    private class ButtonRenderer implements TableCellRenderer {
        private JButton component = new JButton("View");
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return component;
        }
    }

    public OrganisationUnits() {
        makeGui();
    }

    public void setData(List<Unit> data) {
        model.setUnits(data);
    }

    private void makeGui(){
        tblUnits.getColumnModel().getColumn(0).setPreferredWidth(200);
        for (int i = 1; i < model.getColumnCount(); i++) {
            tblUnits.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        tblUnits.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());
        tblUnits.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        units.setLayout(new BorderLayout());
        units.add(tblUnits.getTableHeader(), BorderLayout.PAGE_START);
        units.add(tblUnits, BorderLayout.CENTER);
        setBackground(GuiColours.PANEL);
        setLayout(new GridBagLayout());
        addToPanel(this, lblTitle, Gbc.nu(0,0,1,1).pad(5).west());
        addToPanel(this, units, Gbc.nu(0,1,1,1).pad(5).west());

    }

    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    protected void fireActionPerformed(int unitId) {
        ActionEvent actionEvent = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (actionEvent == null)
                    actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""+unitId);
                ((ActionListener)listeners[i+1]).actionPerformed(actionEvent);
            }
        }
    }

}
