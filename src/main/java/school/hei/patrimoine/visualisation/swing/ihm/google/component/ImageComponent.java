package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageComponent extends JLabel {
  public ImageComponent(String imageUrl, String alt, int size, boolean rounded) {
    setPreferredSize(new Dimension(size, size));
    try {
      var img = ImageIO.read(URI.create(imageUrl).toURL());
      var scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);

      if (rounded) {
        setIcon(new ImageIcon(makeRounded(toBufferedImage(scaled), size)));
      } else {
        setIcon(new ImageIcon(scaled));
      }
    } catch (IOException e) {
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
}
