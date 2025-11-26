package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

    if (!(value instanceof File file)) {
      return label;
    }

    label.setIcon(icon);
    label.setText(file.getName());
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    label.setBorder(new EmptyBorder(6, 12, 6, 12));
    label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4f));

    if (!isSelected) {
      Point mousePos = null;
      try {
        mousePos = list.getMousePosition();
      } catch (IllegalComponentStateException ignored) {
      }

      if (mousePos != null) {
        int hoverIndex = list.locationToIndex(mousePos);
        if (hoverIndex == index && hoverIndex != -1) {
          label.setBackground(new Color(200, 220, 255));
        }
      }
    }

    return label;
  }

  private ImageIcon loadFileIcon() {
    var url = getClass().getResource("/icons/file.png");
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }
}
