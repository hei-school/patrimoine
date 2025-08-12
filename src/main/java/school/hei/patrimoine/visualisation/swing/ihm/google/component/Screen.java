package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import javax.swing.*;

public class Screen extends JFrame {
  public Screen(String title, int width, int height) {
    super(title);

    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
