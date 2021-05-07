package GUI;

import GUI.Classes.Node;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Base extends JFrame{
    private JPanel background;
    private JPanel sideBar;
    private JPanel content;
    private JPanel topBar;
    private JTree menu;
    private String[] unitListItems = {"Create Unit"};
    private String[] assetListItems = {"Create Asset"};
    private String[] userListItems = {"Change Password"};

    public Base() {
        createUIComponents();
    }

    private void createUIComponents() {

        setContentPane(background);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

//        IconUIResource emptyIcon = new IconUIResource(new Icon() {
//            @Override public void paintIcon(Component c, Graphics g, int x, int y) {}
//            @Override public int getIconWidth() {
//                return 0;
//            }
//            @Override public int getIconHeight() {
//                return 0;
//            }
//        });
//
//        UIManager.put("Tree.expandedIcon",  emptyIcon);
//        UIManager.put("Tree.collapsedIcon", emptyIcon);
//        UIManager.put("Tree.paintLines",    Boolean.FALSE);
//
//        UIDefaults d = new UIDefaults();
//        d.put("Tree:TreeCell[Enabled+Selected].backgroundPainter", new Painter<JComponent>() {
//            @Override public void paint(Graphics2D g, JComponent c, int w, int h) {
//                g.setPaint(new Color(133, 16, 245, 255));
//                g.fillRect(0, 0, w, h);
//            }
//        });
//        d.put("Tree:TreeCell[Focused+Selected].backgroundPainter", new Painter<JComponent>() {
//            @Override public void paint(Graphics2D g, JComponent c, int w, int h) {
//                g.setColor(Color.RED);
//                g.fillRect(0, 0, w, h);
//            }
//        });
//        tree1.putClientProperty("Nimbus.Overrides", d);
//        tree1.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.FALSE);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultTreeModel model =(DefaultTreeModel) menu.getModel();
        model.setRoot(root);
        menu.setCellRenderer(new TestTreeCellRenderer());
        menu.setBackground(new Color(0,0,0, 0));
        createTreeNodes(model);
    }

    private static void addNodeToDefaultTreeModel( DefaultTreeModel treeModel, DefaultMutableTreeNode parentNode, DefaultMutableTreeNode node ) {
        treeModel.insertNodeInto(  node, parentNode, parentNode.getChildCount()  );
        if (  parentNode == treeModel.getRoot()  ) {
            treeModel.nodeStructureChanged(  (TreeNode) treeModel.getRoot()  );
        }
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
            node = new DefaultMutableTreeNode(
                    new Node(entry.getKey(), new Color(0,0,0,0), d64, true));
            addNodeToDefaultTreeModel( model, parentNode, node );
            for (int j = 0; j < entry.getValue().length; ++j) {
                parentNode = node;
                node = new DefaultMutableTreeNode(
                        new Node(entry.getValue()[j], new Color(0,0,0,0), d64, false));
                addNodeToDefaultTreeModel( model, parentNode, node );
            }
        }
    }

    class TestTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override public Component getTreeCellRendererComponent(
                JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            JLabel l = (JLabel) super.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object uo = node.getUserObject();
                if (uo instanceof Node) {
                    Node i = (Node) uo;
                    l.setForeground(Color.WHITE);
                    l.setIcon(new Node(i.title, i.color, i.dim, leaf, expanded));

                    int indent = 0;
                    TreeNode parent = node.getParent();
                    while (parent instanceof DefaultMutableTreeNode) {
                        DefaultMutableTreeNode pn = (DefaultMutableTreeNode) parent;
                        if (pn.getUserObject() instanceof Node) {
                            Node pi = (Node) pn.getUserObject();
                            indent += pi.dim.width / 1;
                        }
                        parent = pn.getParent();
                    }
                    l.setBorder(BorderFactory.createEmptyBorder(2, indent, 2, 5));
                }
            }
            return l;
        }
    }
}
