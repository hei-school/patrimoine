package school.hei.patrimoine.visualisation.swing.ihm.google.mode.config;

import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.AppMode;

@Slf4j
public class ModeResolver {
  private static final AppMode CURRENT = resolve();

  private static AppMode resolve() {
    String value = System.getProperty("patrimoine.mode");

    if (value == null || value.isBlank()) {
      log.warn(
          "System property 'patrimoine.mode' is not set or blank. Falling back to environment"
              + " variable 'PATRIMOINE_MODE'.");
      value = System.getenv("PATRIMOINE_MODE");
    }

    if ("OFFLINE".equalsIgnoreCase(value)) {
      return new OfflineMode();
    }

    return new OnlineMode();
  }

  public static AppMode current() {
    return CURRENT;
  }
}
