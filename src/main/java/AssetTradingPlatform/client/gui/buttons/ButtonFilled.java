package AssetTradingPlatform.client.gui.buttons;

import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonFilled extends JButton {
    public ButtonFilled (String text, int width, int height){
        LineBorder border1 = new LineBorder(GuiColours.TITLE, 3, true);
        EmptyBorder border2 = new EmptyBorder(height,width,height,width);
        Border newBorder = BorderFactory.createCompoundBorder(border2, border1);
        this.setText(text);
        this.setBackground(GuiColours.TITLE);
        this.setBorder(newBorder);
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
    }
}
