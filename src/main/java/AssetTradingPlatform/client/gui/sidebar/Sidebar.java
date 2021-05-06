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

    private String[] unitListItems = {"Create Unit"};
    private String[] assetListItems = {"Create Asset"};
    private String[] userListItems = {"Change Password"};

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
        DefaultMutableTreeNode parentNode;
        DefaultMutableTreeNode node;
        Dimension d64 = new Dimension(24, 64);

        map = new HashMap<String, String[]>();
        map.put("Units", unitListItems);
        map.put("Assets", assetListItems);
        map.put("Users", userListItems);

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            parentNode = (DefaultMutableTreeNode) model.getRoot();
            node = new DefaultMutableTreeNode(entry.getKey());
            model.insertNodeInto(node, parentNode, parentNode.getChildCount());
            for (String value: entry.getValue()) {
                parentNode = node;
                node = new DefaultMutableTreeNode(value);
                model.insertNodeInto(node, parentNode, parentNode.getChildCount());
            }
        }

        model.nodeStructureChanged(  (TreeNode) model.getRoot()  );
    }
}
