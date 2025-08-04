package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import javax.swing.*;

public class ButtonWithIcon extends JButton {
  private final Image icon;
  private final Color backgroundColor;
  private final Color textColor;
  private final int borderRadius;
  private final int paddingX = 16;
  private final int paddingY = 10;
  private final int iconSize = 25;
  private final int spacing = 10;

  public ButtonWithIcon(
      String text, Image icon, Color backgroundColor, Color textColor, int borderRadius) {
    super(text);
    this.icon = icon;
    this.backgroundColor = backgroundColor;
    this.textColor = textColor;
    this.borderRadius = borderRadius;

    setContentAreaFilled(false);
    setFocusPainted(false);
    setBorderPainted(false);
    setCursor(new Cursor(Cursor.HAND_CURSOR));
    setFont(new Font("Arial", Font.PLAIN, 16));
  }

  public ButtonWithIcon(String text, Image icon) {
    this(text, icon, new Color(230, 227, 227), Color.BLACK, 12);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();

    // Draw rounded background
    g2.setColor(backgroundColor != null ? backgroundColor : getBackground());
    g2.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);

    // Compute content sizes
    FontMetrics fm = g2.getFontMetrics();
    String text = getText();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getAscent() + fm.getDescent();

    int iconW = icon != null ? iconSize : 0;
    int iconH = icon != null ? iconSize : 0;

    int totalWidth = iconW + (icon != null ? spacing : 0) + textWidth;
    int contentX = (width - totalWidth) / 2;
    int centerY = height / 2;

    // Draw icon
    if (icon != null) {
      int iconY = centerY - iconH / 2;
      g2.drawImage(icon, contentX, iconY, iconW, iconH, null);
      contentX += iconW + spacing;
    }

    // Draw text
    int textY = centerY + (textHeight / 2) - fm.getDescent();
    g2.setColor(textColor != null ? textColor : getForeground());
    g2.drawString(text, contentX, textY);

    g2.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    FontMetrics fm = getFontMetrics(getFont());
    int textWidth = fm.stringWidth(getText());
    int textHeight = fm.getAscent() + fm.getDescent();
    int iconW = icon != null ? iconSize : 0;

    int width = iconW + (icon != null ? spacing : 0) + textWidth + paddingX * 2;
    int height = Math.max(iconSize, textHeight) + paddingY * 2;

    return new Dimension(width, height);
  }
}
