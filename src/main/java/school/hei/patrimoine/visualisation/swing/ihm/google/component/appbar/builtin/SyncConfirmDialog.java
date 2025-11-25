package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedPlannedFiles;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;

public class SyncConfirmDialog extends JDialog {
  @Getter private boolean confirmed = false;

  public SyncConfirmDialog() {
    super((Frame) null, "Confirmer la synchronisation", true);

    List<File> plannedFiles = getStagedPlannedFiles();
    List<File> doneFiles = getStagedDoneFiles();

    boolean hasFiles = !plannedFiles.isEmpty() || !doneFiles.isEmpty();

    if (hasFiles) {
      initConfirmComponents(plannedFiles, doneFiles);
    } else {
      initInfoComponents();
    }

    pack();
    setLocationRelativeTo(null);
  }

  private void initConfirmComponents(List<File> plannedFiles, List<File> doneFiles) {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

    var filesPanel = new JPanel();
    filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));

    addFileSection(filesPanel, "Planifiés", plannedFiles, loadFileIcon());
    addFileSection(filesPanel, "Réalisés", doneFiles, loadFileIcon());

    var scrollPane = new JScrollPane(filesPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    int maxHeight = 600;
    int computedHeight = Math.min(filesPanel.getPreferredSize().height, maxHeight);
    scrollPane.setPreferredSize(new Dimension(450, computedHeight));

    messagePanel.add(scrollPane, BorderLayout.CENTER);

    var questionLabel = new JLabel("Voulez-vous synchroniser ces fichiers avec Google Drive ?");
    questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    add(messagePanel, BorderLayout.CENTER);

    addButtons();
  }

  private void initInfoComponents() {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");
    var iconLabel = new JLabel(infoIcon);

    var messageLabel = new JLabel("Aucun fichier en attente de synchronisation.");
    messageLabel.setFont(UIManager.getFont("Label.font"));

    messagePanel.add(iconLabel, BorderLayout.WEST);
    messagePanel.add(messageLabel, BorderLayout.CENTER);

    add(messagePanel, BorderLayout.CENTER);

    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

    var okButton = new JButton("OK");
    okButton.setPreferredSize(new Dimension(70, okButton.getPreferredSize().height));
    okButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(okButton);

    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);

    setMinimumSize(new Dimension(300, 100));
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

    var cancelButton = new JButton("Annuler");
    cancelButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    var confirmButton = new JButton("Synchroniser");
    confirmButton.setPreferredSize(new Dimension(120, cancelButton.getPreferredSize().height));
    confirmButton.addActionListener(
        e -> {
          confirmed = true;
          dispose();
        });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(cancelButton);
    rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    rightPanel.add(confirmButton);

    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void addFileSection(JPanel container, String title, List<File> files, ImageIcon icon) {
    if (files.isEmpty()) return;

    var label = new JLabel(title);
    label.setFont(new Font("Arial", Font.BOLD, 15));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

    container.add(label);

    for (File file : files) {
      container.add(createFilePanel(file, icon));
      container.add(Box.createVerticalStrut(5));
    }

    container.add(Box.createVerticalStrut(10));
  }

  private JPanel createFilePanel(File file, ImageIcon icon) {
    var filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    filePanel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    var iconLabel = new JLabel(icon);
    var nameLabel = new JLabel(file.getName());
    nameLabel.setFont(UIManager.getFont("Label.font"));

    filePanel.add(iconLabel);
    filePanel.add(nameLabel);

    return filePanel;
  }

  private ImageIcon loadFileIcon() {
    var url = getClass().getResource("/icons/file.png");
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }

  public static boolean showDialog() {
    SyncConfirmDialog dialog = new SyncConfirmDialog();
    dialog.setVisible(true);
    return dialog.isConfirmed();
  }
}
