package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import javax.swing.*;

public class RoundedPanel extends JPanel {
  public RoundedPanel(String text, Color backgroundColor, Color textColor) {
    setOpaque(false);
    setLayout(new BorderLayout());

    var label = new JLabel(text);
    label.setForeground(textColor);
    label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setVerticalAlignment(SwingConstants.CENTER);
    label.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));

    add(label, BorderLayout.CENTER);

    setBackground(backgroundColor);
  }

  @Override
  protected void paintComponent(Graphics g) {
    var g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
    g2.dispose();
    super.paintComponent(g);
  }
}
