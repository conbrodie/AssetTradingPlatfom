package AssetTradingPlatform.client.gui;


import AssetTradingPlatform.client.gui.panels.Login;
import AssetTradingPlatform.client.gui.panels.Register;
import AssetTradingPlatform.client.gui.sidebar.Sidebar;
import AssetTradingPlatform.client.gui.titlebar.TitleBar;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame implements Runnable {

    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 900;

    private JPanel pnlNorth;
    private JTree sidebar;

    private JPanel pnlCenter;
    private JPanel pnlCards;
    private JPanel pnlLogin;
    private JPanel pnlRegister;



    private void createGui() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout mainLayout = new BorderLayout();
        setLayout(mainLayout);
        // create main panel
        pnlCenter = createPanel(GuiColours.CONTENT);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // create Login panel
        pnlLogin = new Login();
        // create Register panel
        pnlRegister = new Register();


        //create panel around edges
        sidebar = new Sidebar();
        getContentPane().add(sidebar, BorderLayout.WEST);
        pnlNorth = new TitleBar();
        pnlNorth.setPreferredSize(new Dimension(FRAME_WIDTH, 50));
        getContentPane().add(pnlNorth, BorderLayout.NORTH);

        // make cards
        pnlCards = new JPanel();
        pnlCards.setLayout(new CardLayout());
        pnlCards.add(pnlLogin, "LOGIN");
        pnlCards.add(pnlRegister, "REGISTER");

        ((CardLayout)pnlCards.getLayout()).show(pnlCards, "LOGIN");

        //position components in the panel
        pnlCenter.setLayout((new GridBagLayout()));
        addToPanel(pnlCenter, pnlCards, Gbc.nu( 0, 0, 1, 1));
        pack();
        setVisible(true);
    }

    private JPanel createPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        return panel;
    }


    private void addToPanel(JPanel jp, Component c, Gbc g) {
        jp.add(c, g.build());
    }

    @Override
    public void run() {
        createGui();
    }

    public static void main(String[] args) {
        Main frameLogin = new Main();
        SwingUtilities.invokeLater(frameLogin);
    }

}