package school.hei.patrimoine.visualisation.swing.ihm.google.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentConfig {
  private static final Mode CURRENT_MODE;

  static {
    CURRENT_MODE = getMode();
  }

  private static Mode getMode() {
    var modeValue = System.getProperty("patrimoine.mode");

    if (modeValue == null || modeValue.trim().isEmpty()) {
      modeValue = System.getenv("PATRIMOINE_MODE");
    }

    if (modeValue == null || modeValue.trim().isEmpty()) {
      return Mode.ONLINE;
    }

    try {
      return Mode.valueOf(modeValue.trim().toUpperCase());
    } catch (Exception e) {
      log.error(
          "Invalid mode: '{}'. Possible values: OFFLINE, ONLINE. \nUsing ONLINE mode by default.",
          modeValue);
      return Mode.ONLINE;
    }
  }

  public static boolean isOnlineMode() {
    return CURRENT_MODE.equals(Mode.ONLINE);
  }

  public static boolean isOfflineMode() {
    return CURRENT_MODE.equals(Mode.OFFLINE);
  }

  public enum Mode {
    OFFLINE,
    ONLINE
  }
}
