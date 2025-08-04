package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

public class Dialog extends JDialog {
    public Dialog(Frame owner, String title, int width, int height) {
        super(owner, title, true);

        var label = new JLabel(title);
        label.setHorizontalAlignment(CENTER);
        getContentPane().add(label, CENTER);
        setLocationRelativeTo(this);
        setSize(width, height);
    }
}
