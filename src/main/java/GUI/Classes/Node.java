package GUI.Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Node implements Icon {
    public final String title;
    public final Color color;
    public final Dimension dim;
    private final boolean expanded;
    private final boolean leaf;
    private static int GAP = 4;

    //public TestNode(String title, ImageIcon img, Dimension dim) {
    public Node(String title, Color color, Dimension dim, boolean leaf) {
        this(title, color, dim, leaf, false);
    }

    public Node(String title, Color color, Dimension dim,
                    boolean leaf, boolean expanded) {
        this.title = title;
        this.color = color;
        this.dim = dim;
        this.expanded = expanded;
        this.leaf = leaf;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x + GAP, y + GAP, dim.width - GAP - GAP, dim.height - GAP - GAP);
        if (dim.width < 64) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int w6 = dim.width / 12;
            int w3 = dim.width / 6;
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(w6));
            Point pt = new Point(x + dim.width / 2, y + dim.height / 2);
            Path2D path = new Path2D.Double();
            path.moveTo(pt.x - w6, pt.y - w3);
            path.lineTo(pt.x + w6, pt.y);
            path.lineTo(pt.x - w6, pt.y + w3);
            int numquadrants;
            if (leaf) {
                numquadrants = 0;
            } else if (expanded) {
                numquadrants = 3;
            } else {
                numquadrants = 1;
            }
            AffineTransform at = AffineTransform.getQuadrantRotateInstance(
                    numquadrants, pt.x, pt.y);
            g2.draw(at.createTransformedShape(path));
            g2.dispose();
        }
    }

    @Override
    public int getIconWidth() {
        return dim.width;
    }

    @Override
    public int getIconHeight() {
        return dim.height;
    }

    @Override
    public String toString() {
        return title;
    }
}
