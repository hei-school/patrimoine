package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import static java.awt.Desktop.Action.BROWSE;

import java.awt.Desktop;
import java.net.URI;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkOpener implements Consumer<String> {
  private static final String[][] LINUX_COMMANDS = {
    {"xdg-open"}, {"gnome-open"}, {"kde-open"}, {"exo-open"}, {"firefox"}
  };

  @Override
  public void accept(String url) {
    var uri = URI.create(url);
    if (!tryDesktop(uri) && !trySystemCommand(url)) {
      log.error("Could not open link: {}", url);
    }
  }

  private boolean tryDesktop(URI uri) {
    try {
      var desktop = Desktop.getDesktop();
      if (!Desktop.isDesktopSupported() || !desktop.isSupported(BROWSE)) {
        return false;
      }
      desktop.browse(uri);
      return true;
    } catch (Exception e) {
      log.debug("Desktop.browse() failed: {}", e.getMessage());
      return false;
    }
  }

  private boolean trySystemCommand(String url) {
    try {
      var os = System.getProperty("os.name").toLowerCase();
      String[] cmd =
          os.contains("win")
              ? new String[] {"cmd", "/c", "start", url}
              : os.contains("mac") ? new String[] {"open", url} : linuxCommand(url);
      if (cmd == null) return false;
      new ProcessBuilder(cmd).start().waitFor();
      return true;
    } catch (Exception e) {
      log.debug("System command failed: {}", e.getMessage());
      return false;
    }
  }

  private String[] linuxCommand(String url) {
    for (String[] cmd : LINUX_COMMANDS) {
      try {
        if (new ProcessBuilder("which", cmd[0]).start().waitFor() == 0)
          return new String[] {cmd[0], url};
      } catch (Exception ignored) {
      }
    }
    return null;
  }
}
