package school.hei.patrimoine.visualisation.swing.ihm.google.component.app;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    public Screen(String title, int width, int height) {
        super(title);

        setTitle(title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(width, height));

        pack();

        setVisible(true);
    }
}
