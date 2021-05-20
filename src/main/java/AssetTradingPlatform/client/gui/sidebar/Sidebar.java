package AssetTradingPlatform.client.gui.sidebar;

import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Sidebar extends JTree {

    public Sidebar() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model =(DefaultTreeModel) getModel();
        model.setRoot(root);
        createTreeNodes(model);
        setRootVisible(false);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)getCellRenderer();
        renderer.setClosedIcon(new SidebarIcon(false, false));
        renderer.setOpenIcon(new SidebarIcon(false, true));
        renderer.setLeafIcon(new SidebarIcon(true, false));
        renderer.setOpaque(true);
        renderer.setBackgroundNonSelectionColor(GuiColours.MENU);
        renderer.setBackgroundSelectionColor(GuiColours.MENU);
        renderer.setBorderSelectionColor(GuiColours.MENU);
        renderer.setTextSelectionColor(GuiColours.TEXT);
        renderer.setTextNonSelectionColor(GuiColours.TEXT);
        setPreferredSize(new Dimension(200, 500));
        setBackground(GuiColours.MENU);
    }

    private static void addNodeToDefaultTreeModel(DefaultTreeModel treeModel, DefaultMutableTreeNode parentNode, DefaultMutableTreeNode node ) {
        treeModel.insertNodeInto(  node, parentNode, parentNode.getChildCount()  );
    }

    private void createTreeNodes(DefaultTreeModel model) {

        Map<String, String[]> map;


        Dimension d64 = new Dimension(24, 64);

        map = new HashMap<String, String[]>();
        map.put("Trade", new String[]{ });
        map.put("Login", new String[]{ });
        map.put("Users", new String[]{"Register", "Change Password"});
        map.put("Organisations", new String[]{"Organisation Units"});

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(entry.getKey());
            model.insertNodeInto(category, root, root.getChildCount());
            for (String value: entry.getValue()) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(value);
                model.insertNodeInto(child, category, category.getChildCount());
            }
        }

        model.nodeStructureChanged(  (TreeNode) model.getRoot()  );
    }
}
