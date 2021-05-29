package client;

import javax.swing.*;

/**
 * Swing Application
 */

public class App 
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("Layout");
            }
        });
    }
}
