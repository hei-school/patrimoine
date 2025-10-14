package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static javax.swing.filechooser.FileSystemView.getFileSystemView;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

public class FileListCellRenderer extends DefaultListCellRenderer {
  private final FileSystemView fileSystemView;

  public FileListCellRenderer() {
    this.fileSystemView = getFileSystemView();
  }

  @Override
  public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    var label =
        (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (!(value instanceof File file)) {
      return label;
    }

    label.setIcon(fileSystemView.getSystemIcon(file));
    label.setText(file.getName());
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    label.setBorder(new EmptyBorder(6, 12, 6, 12));
    label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 4f));

    if (!isSelected) {
      Point mousePos = null;
      try {
        mousePos = list.getMousePosition();
      } catch (IllegalComponentStateException ignored) {}

      if (mousePos != null) {
        int hoverIndex = list.locationToIndex(mousePos);
        if (hoverIndex == index && hoverIndex != -1) {
          label.setBackground(new Color(200, 220, 255));
        }
      }
    }

    return label;
  }
}
