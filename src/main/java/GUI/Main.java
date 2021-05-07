package GUI;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(laf.getName())) {
                        UIManager.setLookAndFeel(laf.getClassName());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Base base = new Base();
        });
    }
}
