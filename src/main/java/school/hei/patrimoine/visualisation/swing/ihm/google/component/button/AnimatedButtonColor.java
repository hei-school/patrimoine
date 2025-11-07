package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import java.awt.*;

public class AnimatedButtonColor {
  private final Color baseColor1;
  private final Color baseColor2;
  private final Color hoverColor1;
  private final Color hoverColor2;

  public AnimatedButtonColor(
      Color baseColor1, Color baseColor2, Color hoverColor1, Color hoverColor2) {
    this.baseColor1 = baseColor1;
    this.baseColor2 = baseColor2;
    this.hoverColor1 = hoverColor1;
    this.hoverColor2 = hoverColor2;
  }

  public Color getInterpolatedColor1(float t) {
    return interpolate(baseColor1, hoverColor1, t);
  }

  public Color getInterpolatedColor2(float t) {
    return interpolate(baseColor2, hoverColor2, t);
  }

  private Color interpolate(Color c1, Color c2, float t) {
    var r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * t);
    var g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
    var b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
    return new Color(r, g, b);
  }
}
