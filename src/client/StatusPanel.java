package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel {

    private IStatusPanelListener statusPanelListener;
    private JLabel organisationLabel = new JLabel("Organisation:  ");
    private JLabel usernameLabel = new JLabel("Logged in as:  ");
    private JCheckBox filterCheckbox = new JCheckBox("Filter");
    private JButton refreshButton = new JButton("Refresh Trades");

    public StatusPanel() {
        organisationLabel.setForeground(Color.decode("#346FF6"));
        usernameLabel.setForeground(Color.decode("#346FF6"));

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(8,10,12,0);
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(8,2,12,0);
        gbc.anchor = GridBagConstraints.WEST;
        add(organisationLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(8,10,12,0);
        gbc.anchor = GridBagConstraints.WEST;
        filterCheckbox.setEnabled(false);
        add(filterCheckbox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(8,0,12,15);
        gbc.anchor = GridBagConstraints.EAST;
        add(refreshButton, gbc);

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusPanelListener.onRefreshButtonClick();
            }
        });
        filterCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusPanelListener.onFilterSelect(filterCheckbox.isSelected());
            }
        });
    }

    public String getOrgUnit() {
        return organisationLabel.getText().substring(organisationLabel.getText().lastIndexOf(':') +1 ).trim();
    }

    public void setStatusPanelListener(IStatusPanelListener listener) {
        this.statusPanelListener = listener;
    }
}
