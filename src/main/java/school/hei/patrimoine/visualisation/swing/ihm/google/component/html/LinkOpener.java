package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import java.awt.Desktop;
import java.net.URI;
import java.util.function.Consumer;

public class LinkOpener implements Consumer<String> {
  @Override
  public void accept(String url) {
    try {
      if (!Desktop.isDesktopSupported()) {
        throw new UnsupportedOperationException("Desktop API not supported");
      }

      Desktop.getDesktop().browse(URI.create(url));
    } catch (Exception e) {
      throw new RuntimeException("Failed to open link: " + url, e);
    }
  }
}
