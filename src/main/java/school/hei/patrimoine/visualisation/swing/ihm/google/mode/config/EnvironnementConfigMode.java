package school.hei.patrimoine.visualisation.swing.ihm.google.mode.config;

import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.AppMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.OfflineMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.mode.OnlineMode;

@Slf4j
public class EnvironnementConfigMode {
  private static final AppMode CURRENT = resolve();
  private static final String OFFLINE_MODE_ENV_VALUE = "OFFLINE";

  private static AppMode resolve() {
    String value = System.getProperty("patrimoine.mode");

    if (value == null || value.isBlank()) {
      value = System.getenv("PATRIMOINE_MODE");
    }

    if (OFFLINE_MODE_ENV_VALUE.equalsIgnoreCase(value)) {
      return new OfflineMode();
    }

    return new OnlineMode();
  }

  public static AppMode getCurrentMode() {
    return CURRENT;
  }
}
