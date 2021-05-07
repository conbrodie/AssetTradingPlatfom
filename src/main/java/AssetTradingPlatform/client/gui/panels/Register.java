package AssetTradingPlatform.client.gui.panels;

import AssetTradingPlatform.client.gui.Gbc;
import AssetTradingPlatform.client.gui.GuiColours;

import javax.swing.*;
import java.awt.*;

public class Register extends JPanel {
    private JLabel lblTitle = new JLabel("Register");
    private JLabel lblUsername = new JLabel("Username");
    private JTextField username = new JTextField(30);
    private JLabel lblPassword = new JLabel("Password");
    private JTextField password = new JTextField(30);
    private JLabel lblConfirmPassword = new JLabel("Confirm Password");
    private JTextField confirmPassword = new JTextField(30);
    private JLabel lblOrganisation = new JLabel("Organisation");
    private JComboBox<String> organisation = new JComboBox<>(new String[] { "Org 1"});
    private JButton btnRegister = new JButton("Register");

    public Register() {
        createGui();
    }

    private void createGui() {
        setBackground(GuiColours.PANEL);
        setLayout(new GridBagLayout());
        addToPanel(this, lblTitle, Gbc.nu(0, 0, 2, 1).pad(5).west());
        Gbc lgc = Gbc.nu().west().pad(5).padLeft(20);
        addToPanel(this, lblUsername, lgc.xy( 0, 1, 1, 1));
        addToPanel(this, username, Gbc.nu(1, 1, 1, 1).pad(5));
        addToPanel(this, lblPassword, lgc.xy(0, 2, 1, 1));
        addToPanel(this, password, Gbc.nu(1, 2, 1, 1).pad(5));
        addToPanel(this, lblConfirmPassword, lgc.xy(0, 3, 1, 1));
        addToPanel(this, confirmPassword, Gbc.nu(1, 3, 1, 1).pad(5));
        addToPanel(this, lblOrganisation, lgc.xy(0, 4, 1, 1));
        addToPanel(this, organisation, Gbc.nu(1, 4, 1, 1).horizontal().pad(5));
        addToPanel(this, btnRegister, Gbc.nu(0, 5, 2, 1).pad(5));

    }
    private void addToPanel(JPanel jp, Component c, Gbc g) {
        jp.add(c, g.build());
    }
}
