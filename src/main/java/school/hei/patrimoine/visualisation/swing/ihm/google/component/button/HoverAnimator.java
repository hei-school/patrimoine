package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import javax.swing.*;
import lombok.Getter;
import lombok.Setter;

public class HoverAnimator {
  @Getter private float progress = 0f;
  @Setter private boolean hover = false;
  private final Timer timer;

  public HoverAnimator(Runnable repaintCallback) {
    timer =
        new Timer(
            15,
            e -> {
              var speed = 0.08f;
              if (hover && progress < 1f) {
                progress = Math.min(1f, progress + speed);
                repaintCallback.run();
              } else if (!hover && progress > 0f) {
                progress = Math.max(0f, progress - speed);
                repaintCallback.run();
              }
            });
    timer.start();
  }
}
