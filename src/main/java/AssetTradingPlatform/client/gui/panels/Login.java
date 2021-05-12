package AssetTradingPlatform.client.gui.panels;

import AssetTradingPlatform.client.gui.Gbc;
import AssetTradingPlatform.client.gui.GuiColours;
import AssetTradingPlatform.client.gui.buttons.ButtonOutline;

import javax.swing.*;
import java.awt.*;
import static AssetTradingPlatform.client.gui.Gbc.addToPanel;

public class Login extends JPanel {
    private JLabel lblTitle = new JLabel("Login");
    private JLabel lblUsername = new JLabel("Username");
    private JTextField username = new JTextField(30);
    private JLabel lblPassword = new JLabel("Password");
    private JTextField password = new JTextField(30);;
    private ButtonOutline btnLogin = new ButtonOutline("Login");

    public Login() {
        loginGui();
    }

    private void loginGui() {
        setBackground(GuiColours.PANEL);
        setLayout(new GridBagLayout());
        Gbc lgc = Gbc.nu().west().pad(5).padLeft(20);
        addToPanel(this, lblTitle, Gbc.nu(0, 0, 2, 1).pad(5).west());
        addToPanel(this, lblUsername, lgc.xy( 0, 1, 1, 1));
        addToPanel(this, username, Gbc.nu(1, 1, 1, 1).pad(5));
        addToPanel(this, lblPassword, lgc.xy(0, 2, 1, 1));
        addToPanel(this, password, Gbc.nu(1, 2, 1, 1).pad(5));
        addToPanel(this, btnLogin, Gbc.nu(0, 3, 2, 1).pad(5));

    }
}
