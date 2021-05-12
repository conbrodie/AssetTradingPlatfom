package AssetTradingPlatform.client.gui;

import javax.swing.*;
import java.awt.*;

public class Gbc {
    private int x;
    private int y;
    private int w;
    private int h;
    private int anchor;
    private int fill;
    private int insetLeft;
    private int insetTop;
    private int insetRight;
    private int insetBottom;
    private double weightX;
    private double weightY;


    private Gbc(GridBagConstraints c) {
        this.x = c.gridx;
        this.y = c.gridy;
        this.w = c.gridwidth;
        this.h = c.gridheight;
        this.anchor = c.anchor;
        this.fill = c.fill;
        this.insetLeft = c.insets.left;
        this.insetBottom = c.insets.bottom;
        this.insetRight = c.insets.right;
        this.insetTop = c.insets.top;
        this.weightX = c.weightx;
        this.weightY = c.weighty;
    }

    private Gbc(int x, int y, int w, int h, int anchor, int fill, int insetLeft, int insetBottom, int insetRight, int insetTop, double weightX, double weightY) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.anchor = anchor;
        this.fill = fill;
        this.insetLeft = insetLeft;
        this.insetBottom = insetBottom;
        this.insetRight = insetRight;
        this.insetTop = insetTop;
        this.weightX = weightX;
        this.weightY = weightY;
    }

    public Gbc xy(int x, int y) {
        return xy(x, y, w, h);
    }
    public Gbc xy(int x, int y, int w, int h) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }

    public Gbc weightX(int weightX) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }

    public Gbc weightY(int weightY) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }

    public Gbc anchor(int anchor) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }
    public Gbc lineStart() {
        return anchor(GridBagConstraints.LINE_START);
    }
    public Gbc east() {
        return anchor(GridBagConstraints.EAST);
    }

    public Gbc lineEnd() {
        return anchor(GridBagConstraints.LINE_END);
    }
    public Gbc west() {
        return anchor(GridBagConstraints.WEST);
    }
    public Gbc center() {
        return anchor(GridBagConstraints.CENTER);
    }

    public Gbc fill(int fill) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }

    public Gbc both() {
        return fill(GridBagConstraints.BOTH);
    }

    public Gbc horizontal() {
        return fill(GridBagConstraints.HORIZONTAL);
    }

    public Gbc pad(int inset) {
        return new Gbc(x, y, w, h, anchor, fill, inset, inset, inset, inset, weightX, weightY);
    }

    public Gbc padLeft(int insetLeft) {
        return new Gbc(x, y, w, h, anchor, fill, insetLeft, insetBottom, insetRight, insetTop, weightX, weightY);
    }

    public GridBagConstraints build() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = y;
        g.gridwidth = w;
        g.gridheight = h;
        g.anchor = anchor;
        g.fill = fill;
        g.insets.left = insetLeft;
        g.insets.right = insetRight;
        g.insets.top = insetTop;
        g.insets.bottom = insetBottom;
        g.weightx = weightX;
        g.weighty = weightY;
        return g;
    }


    public static Gbc nu() {
//        GridBagConstraints c = new GridBagConstraints();
//        return new Gbc(c.gridx, c.gridy, c.gridwidth, c.gridheight, c.anchor, c.fill, c.insets.left, c.insets.bottom, c.insets.right, c.insets.top);
        return new Gbc(new GridBagConstraints());
    }

    public static Gbc nu(int x, int y) {
        return Gbc.nu().xy(x, y);
    }

    public static Gbc nu(int x, int y, int w, int h) {
        return Gbc.nu().xy(x, y, w, h);
    }

    public static void addToPanel(JPanel jp, Component c, Gbc g) {
        jp.add(c, g.build());
    }
}

