package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import javax.swing.border.AbstractBorder;

public class CustomBorder extends AbstractBorder {
  private final Color borderColor;
  private final int thickness;
  private final int radius;
  private final boolean shadowEnabled;
  private final Color shadowColor;
  private final int shadowOffset;
  private final Insets padding;

  private CustomBorder(Builder builder) {
    this.borderColor = builder.borderColor;
    this.thickness = builder.thickness;
    this.radius = builder.radius;
    this.shadowEnabled = builder.shadowEnabled;
    this.shadowColor = builder.shadowColor;
    this.shadowOffset = builder.shadowOffset;
    this.padding = builder.padding;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    if (shadowEnabled) {
      g2d.setColor(shadowColor);
      g2d.fillRoundRect(x + shadowOffset, y + shadowOffset, width - 1, height - 1, radius, radius);
    }

    if (borderColor != null && thickness > 0) {
      g2d.setColor(borderColor);
      g2d.setStroke(new BasicStroke(thickness));
      g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    g2d.dispose();
  }

  @Override
  public Insets getBorderInsets(Component c) {
    int offset = shadowEnabled ? shadowOffset : 0;
    return new Insets(
        thickness + padding.top,
        thickness + padding.left,
        thickness + padding.bottom + offset,
        thickness + padding.right + offset);
  }

  @Override
  public Insets getBorderInsets(Component c, Insets insets) {
    int offset = shadowEnabled ? shadowOffset : 0;
    insets.top = thickness + padding.top;
    insets.left = thickness + padding.left;
    insets.bottom = thickness + padding.bottom + offset;
    insets.right = thickness + padding.right + offset;
    return insets;
  }

  public static class Builder {
    private Color borderColor = Color.BLACK;
    private int thickness = 1;
    private int radius = 0;
    private final boolean shadowEnabled = false;
    private final Color shadowColor = new Color(0, 0, 0, 50);
    private final int shadowOffset = 3;
    private Insets padding = new Insets(5, 20, 5, 20);

    public Builder borderColor(Color color) {
      this.borderColor = color;
      return this;
    }

    public Builder thickness(int thickness) {
      this.thickness = thickness;
      return this;
    }

    public Builder radius(int radius) {
      this.radius = radius;
      return this;
    }

    public Builder padding(int vertical, int horizontal) {
      this.padding = new Insets(vertical, horizontal, vertical, horizontal);
      return this;
    }

    public CustomBorder build() {
      return new CustomBorder(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
