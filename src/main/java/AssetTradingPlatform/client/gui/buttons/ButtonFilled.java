package AssetTradingPlatform.client.gui.buttons;

import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonFilled extends JButton {
    public ButtonFilled (String text){
        LineBorder border1 = new LineBorder(GuiColours.TITLE);
        EmptyBorder border2 = new EmptyBorder(5,10,5,10);
        Border newBorder = BorderFactory.createCompoundBorder(border1, border2);
        this.setText(text);
        this.setBackground(GuiColours.TITLE);
        this.setBorder(new LineBorder(GuiColours.TITLE));
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
    }
}
