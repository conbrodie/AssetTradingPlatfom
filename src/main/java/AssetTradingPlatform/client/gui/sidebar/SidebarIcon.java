package AssetTradingPlatform.client.gui.sidebar;

import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class SidebarIcon implements Icon {
    public final Dimension dim = new Dimension(24, 64);;
    private final boolean expanded;
    private final boolean leaf;
    private static int GAP = 4;

    public SidebarIcon(boolean leaf, boolean expanded) {
        this.expanded = expanded;
        this.leaf = leaf;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        c.setBackground(GuiColours.MENU);
        g.fillRect(x + GAP, y + GAP, dim.width - GAP - GAP, dim.height - GAP - GAP);
        if (dim.width < 64) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int w6 = dim.width / 12;
            int w3 = dim.width / 6;
            g2.setColor(GuiColours.TEXT);
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
}
