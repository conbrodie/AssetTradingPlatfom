package AssetTradingPlatform.client.gui.titlebar;

import AssetTradingPlatform.client.gui.Gbc;
import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.*;
import java.awt.*;

public class TitleBar extends JPanel {
    private JLabel logo;
    private JLabel userPlaceholder;

    public TitleBar() {
        setupGui();
    }

    private void setupGui() {
        setBackground(GuiColours.TITLE);
        logo = new JLabel("LOGO");
        logo.setForeground(GuiColours.TEXT);
        userPlaceholder = new JLabel("user placeholder");
        userPlaceholder.setForeground(GuiColours.TEXT);

        setLayout(new GridBagLayout());
        add(logo, Gbc.nu().west().pad(5).weightX(1).horizontal().build());
        add(userPlaceholder, Gbc.nu().east().pad(5).build());
    }
}
