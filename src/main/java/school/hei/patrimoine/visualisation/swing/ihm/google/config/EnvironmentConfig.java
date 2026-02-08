package school.hei.patrimoine.visualisation.swing.ihm.google.config;

import static school.hei.patrimoine.visualisation.swing.ihm.google.Mode.OFFLINE;
import static school.hei.patrimoine.visualisation.swing.ihm.google.Mode.ONELINE;

import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.Mode;

@Slf4j
public class EnvironmentConfig {
  private static final Mode CURRENT_MODE;

  static {
    CURRENT_MODE = determineMode();
  }

  private static Mode determineMode() {
    String modeValue = System.getProperty("patrimoine.mode");

    if (modeValue == null || modeValue.trim().isEmpty()) {
      modeValue = System.getenv("PATRIMOINE_MODE");
    }

    if (modeValue == null || modeValue.trim().isEmpty()) {
      log.warn("Unspecified mode. Uses ONELINE mode by default.");
      return ONELINE;
    }

    try {
      return Mode.valueOf(modeValue.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error("Invalid mode: '" + modeValue + "'. Possible values: OFFLINE, ONELINE");
      log.error("Using ONELINE mode by default.");
      return ONELINE;
    }
  }

  public static boolean isOnelineMode() {
    return CURRENT_MODE == ONELINE;
  }

  public static boolean isOfflineMode() {
    return CURRENT_MODE == OFFLINE;
  }
}
