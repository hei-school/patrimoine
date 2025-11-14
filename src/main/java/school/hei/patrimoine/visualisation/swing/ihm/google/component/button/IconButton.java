package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import java.awt.*;
import javax.swing.*;

public class IconButton extends JButton {
  private final Image icon;
  private final int iconSize;

  public IconButton(Image icon, int iconSize) {
    this.icon = icon;
    this.iconSize = iconSize;

    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
    setOpaque(false);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (icon != null) {
      int width = getWidth();
      int height = getHeight();
      int iconX = (width - iconSize) / 2;
      int iconY = (height - iconSize) / 2;
      g2.drawImage(icon, iconX, iconY, iconSize, iconSize, this);
    }

    g2.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    int padding = 6;
    return new Dimension(iconSize + padding, iconSize + padding);
  }
}
