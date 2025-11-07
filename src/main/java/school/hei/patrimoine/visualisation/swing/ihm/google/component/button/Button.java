package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import static java.awt.Cursor.HAND_CURSOR;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Button extends JButton {
  private final int borderRadius = 12;
  private final AnimatedButtonColor colors;
  private final HoverAnimator animator;

  public Button(String text) {
    super(text);

    setCursor(new Cursor(HAND_CURSOR));
    setMargin(new Insets(8, 16, 8, 16));
    setVerticalAlignment(CENTER);

    setFocusPainted(false);
    setBorderPainted(false);
    setContentAreaFilled(false);
    setForeground(Color.WHITE);
    setFont(getFont().deriveFont(Font.PLAIN, 14f));

    colors =
        new AnimatedButtonColor(
            new Color(66, 103, 178),
            new Color(43, 67, 117),
            new Color(60, 100, 188),
            new Color(66, 103, 178));

    animator = new HoverAnimator(this::repaint);

    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            animator.setHover(true);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            animator.setHover(false);
          }
        });
  }

  public Button(String text, ActionListener actionListener) {
    this(text);

    addActionListener(actionListener);
  }

  @Override
  protected void paintComponent(Graphics g) {
    var g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    var width = getWidth();
    var height = getHeight();

    var t = animator.getProgress();
    var color1 = colors.getInterpolatedColor1(t);
    var color2 = colors.getInterpolatedColor2(t);

    var gradient = new GradientPaint(0, 0, color1, 0, height, color2);
    g2.setPaint(gradient);
    g2.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);

    g2.setColor(new Color(255, 255, 255, 60));
    g2.drawRoundRect(0, 0, width - 1, height - 1, borderRadius, borderRadius);

    super.paintComponent(g2);
    g2.dispose();
  }
}
