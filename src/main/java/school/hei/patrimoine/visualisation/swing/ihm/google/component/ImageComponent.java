package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.*;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;

public class ImageComponent extends JLabel {
  private final ApiCache apiCache;
  private static final String IMAGE_CACHE_KEY = "images";

  public ImageComponent(String imageUrl, String alt, int size, boolean rounded) {
    this.apiCache = ApiCache.getInstance();
    setPreferredSize(new Dimension(size, size));

    try {
      var img = getImage(imageUrl);
      var scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);

      if (rounded) {
        setIcon(new ImageIcon(makeRounded(toBufferedImage(scaled), size)));
      } else {
        setIcon(new ImageIcon(scaled));
      }
    } catch (GoogleIntegrationException e) {
      setText(alt);
    }
  }

  private BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }

    var image =
        new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    var g = image.createGraphics();
    g.drawImage(img, 0, 0, null);
    g.dispose();
    return image;
  }

  private BufferedImage makeRounded(BufferedImage image, int diameter) {
    var masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
    var g2d = masked.createGraphics();
    g2d.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
    g2d.drawImage(image, 0, 0, diameter, diameter, null);
    g2d.dispose();
    return masked;
  }

  public BufferedImage getImage(String url) throws GoogleIntegrationException {
    return apiCache
        .wrap(
            IMAGE_CACHE_KEY,
            url,
            () -> {
              try {
                return ImageIO.read(URI.create(url).toURL());
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .get();
  }
}
