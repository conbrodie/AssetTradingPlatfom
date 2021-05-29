package client;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

    private JLabel tradeMessageLabel = new JLabel("Display area for your organisation's latest trade!");

    MessagePanel() {

        this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        this.setBackground(Color.orange);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(8,10,8,10);
        gbc.anchor = GridBagConstraints.WEST;
        add(tradeMessageLabel, gbc);
    }
}
