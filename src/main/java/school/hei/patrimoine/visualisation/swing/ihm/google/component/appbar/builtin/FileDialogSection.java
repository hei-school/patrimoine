package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FileDialogSection implements DialogSection {
  private final String title;
  private final List<PatriLangFileContext> files;
  private final String iconPath;

  public FileDialogSection(String title, List<PatriLangFileContext> files, String iconPath) {
    this.title = title;
    this.files = files;
    this.iconPath = iconPath;
  }

  @Override
  public boolean isEmpty() {
    return files.isEmpty();
  }

  @Override
  public void addTo(JPanel container) {
    container.add(getSectionTitle(title));
    var icon = getIcon(iconPath);
    for (var file : files) {
      container.add(getOneFilePanel(file, icon));
      container.add(Box.createVerticalStrut(5));
    }
    container.add(Box.createVerticalStrut(10));
  }

  private JPanel getOneFilePanel(PatriLangFileContext file, ImageIcon icon) {
    var panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    panel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(new JLabel(icon));
    panel.add(new JLabel(file.getBaseFileName()));
    return panel;
  }

  private static ImageIcon getIcon(String path) {
    var url = FileDialogSection.class.getResource(path);
    var icon = new ImageIcon(Objects.requireNonNull(url));
    var scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }

  private static JLabel getSectionTitle(String message) {
    var label = new JLabel(message);
    label.setFont(new Font("Arial", Font.BOLD, 15));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    label.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 3, 0).build());
    return label;
  }
}
