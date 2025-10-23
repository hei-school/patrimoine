package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import javax.swing.*;

public class Debouncer {
  private static final int DEFAULT_DELAY = 500;
  private final Timer timer;

  public Debouncer(int delay, boolean repeat, Runnable runnable) {
    this.timer = new Timer(delay, (e) -> runnable.run());
    this.timer.setRepeats(repeat);
  }

  public Debouncer(Runnable runnable) {
    this(DEFAULT_DELAY, false, runnable);
  }

  public void restart() {
    this.timer.restart();
  }
}
