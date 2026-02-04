package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import java.awt.Desktop;
import java.net.URI;
import java.util.function.Consumer;

public class LinkOpener implements Consumer<String> {
  @Override
  public void accept(String url) {
    try {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(new URI(url));
        return;
      }

      String os = System.getProperty("os.name").toLowerCase();
      ProcessBuilder processBuilder;

      if (os.contains("win")) {
        processBuilder = new ProcessBuilder("cmd", "/c", "start", "", url);
      } else if (os.contains("mac")) {
        processBuilder = new ProcessBuilder("open", url);
      } else {
        processBuilder = new ProcessBuilder("xdg-open", url);
      }

      processBuilder.start();
    } catch (Exception e) {
      throw new RuntimeException("Failed to open link: " + url, e);
    }
  }
}
