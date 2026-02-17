package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;

import java.net.URI;
import java.util.function.Consumer;

public class LinkOpener implements Consumer<String> {
  public void accept(URI uri) {
    try {
      if (!isDesktopSupported()) {
        throw new UnsupportedOperationException("Desktop API not supported");
      }

      getDesktop().browse(uri);
    } catch (Exception e) {
      throw new RuntimeException("Failed to open link: " + uri.toString(), e);
    }
  }

  @Override
  public void accept(String url) {
    accept(URI.create(url));
  }
}
