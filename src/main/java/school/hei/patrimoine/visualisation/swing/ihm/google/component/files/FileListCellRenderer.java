package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static java.awt.Color.WHITE;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FileListCellRenderer extends DefaultListCellRenderer {
  private final ImageIcon icon;

  public FileListCellRenderer() {
    this.icon = loadFileIcon();
  }

  @Override
  public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    var label =
        (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (!(value instanceof PatriLangFileContext file)) {
      return label;
    }

    label.setText(file.getName());
    label.setIcon(isModified(file) ? getModifiedIcon() : icon);
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    label.setBorder(new EmptyBorder(6, 12, 6, 12));
    label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4f));

    if (!isSelected) {
      label.setBackground(WHITE);
    }

    return label;
  }

  private static boolean isModified(PatriLangFileContext file) {
    return getAllModifiedFiles().stream()
        .anyMatch(input -> input.file().getAbsolutePath().equals(file.getAbsolutePath()));
  }

  private ImageIcon getModifiedIcon() {
    int dotSize = 8;
    int gap = 4;
    int totalWidth = icon.getIconWidth() + dotSize + gap;
    int height = icon.getIconHeight();

    var combined = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_ARGB);
    var g = combined.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(new Color(215, 1, 1, 255));
    g.fillOval(0, (height - dotSize) / 2, dotSize, dotSize);

    g.drawImage(icon.getImage(), dotSize + gap, 0, null);
    g.dispose();

    return new ImageIcon(combined);
  }

  private ImageIcon loadFileIcon() {
    var url = getClass().getResource("/icons/file.png");

    var icon = new ImageIcon(requireNonNull(url));
    var scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }
}
