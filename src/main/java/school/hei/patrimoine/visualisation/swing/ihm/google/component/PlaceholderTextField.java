package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

public class PlaceholderTextField extends JTextField {
  private final String placeholder;

  public PlaceholderTextField(String placeholder) {
    this.placeholder = placeholder;

    this.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            repaint();
          }

          @Override
          public void focusLost(FocusEvent e) {
            repaint();
          }
        });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (getText().isEmpty() && !isFocusOwner()) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(Color.GRAY);
      g2.setFont(getFont().deriveFont(Font.ITALIC));
      Insets insets = getInsets();
      g2.drawString(
          placeholder, insets.left + 5, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
      g2.dispose();
    }
  }
}
