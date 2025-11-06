package school.hei.patrimoine.visualisation.swing.ihm.google.component.button;

import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.Debouncer;

public class HoverAnimator {
  @Getter private float progress = 0f;
  private boolean hover = false;

  private final Debouncer timer;
  @Getter private final Runnable repaintCallback;

  public HoverAnimator(Runnable repaintCallback) {
    this.repaintCallback = repaintCallback;
    timer =
        new Debouncer(
            15,
            false,
            () -> {
              var speed = 0.08f;
              var updated = false;

              if (hover && progress < 1f) {
                progress = Math.min(1f, progress + speed);
                updated = true;
              } else if (!hover && progress > 0f) {
                progress = Math.max(0f, progress - speed);
                updated = true;
              }
              if (updated) {
                repaintCallback.run();
                start();
              }
            });
  }

  public void setHover(boolean hover) {
    this.hover = hover;
    start();
  }

  public void start() {
    timer.restart();
  }
}
