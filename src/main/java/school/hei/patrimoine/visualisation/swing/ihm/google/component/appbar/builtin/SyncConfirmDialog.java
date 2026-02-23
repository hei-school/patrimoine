package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.GroupedByComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager;

public class SyncConfirmDialog extends Dialog {
  @Getter private boolean confirmed = false;

  static final String FILE_ICON_PATH = "/icons/file.png";
  static final String COMMENT_ICON_PATH = "/icons/comment.png";

  public SyncConfirmDialog() {
    super("Confirmer la synchronisation", 800, 500, false);

    setLayout(new BorderLayout());
    setResizable(false);

    addContentPanel(this, "Voulez-vous synchroniser ces modifications avec Google Drive ?");
    addButtons();

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private static void addFileSection(JPanel contentPanel) {
    var fileIcon = getIcon(FILE_ICON_PATH);
    var pjFiles = PatriLangStagingFileManager.getPJFiles();
    var doneFiles = PatriLangStagingFileManager.getDoneFiles();
    var plannedFiles = PatriLangStagingFileManager.getPlannedFiles();

    if (!plannedFiles.isEmpty()) {
      addFileSection(contentPanel, "Planifiés", plannedFiles, fileIcon);
    }

    if (!doneFiles.isEmpty()) {
      addFileSection(contentPanel, "Réalisés", doneFiles, fileIcon);
    }

    if (!pjFiles.isEmpty()) {
      addFileSection(contentPanel, "Pièces justificatives", pjFiles, fileIcon);
    }
  }

  static void addContentPanel(Dialog parent, String message) {
    var stagingFiles = PatriLangStagingFileManager.getFiles();
    var pendingComments = PendingCommentManager.getPendings();

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 15).build());

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    if (!stagingFiles.isEmpty()) {
      addFileSection(contentPanel);
    }

    if (!pendingComments.isEmpty()) {
      addCommentSection(contentPanel, pendingComments);
    }

    var scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(CustomBorder.builder().thickness(0).padding(0, 0).build());
    scrollPane.setPreferredSize(new Dimension(500, 700));
    messagePanel.add(scrollPane, BorderLayout.CENTER);

    var questionLabel = new JLabel(message);
    questionLabel.setBorder(CustomBorder.builder().thickness(0).padding(10, 0, 0, 0).build());
    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    parent.add(messagePanel, BorderLayout.CENTER);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new BorderLayout());

    var cancelButton =
        new Button(
            "Annuler",
            e -> {
              confirmed = false;
              dispose();
            });

    var confirmButton =
        new Button(
            "Synchroniser",
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

  private static void addFileSection(
      JPanel container, String title, List<PatriLangFileContext> files, ImageIcon icon) {
    container.add(getSectionTitle(title));

    for (var file : files) {
      container.add(getOneFilePanel(file, icon));
      container.add(Box.createVerticalStrut(5));
    }

    container.add(Box.createVerticalStrut(10));
  }

  private static void addCommentSection(JPanel container, Collection<GroupedByComment> pendings) {
    container.add(getSectionTitle("Commentaires modifiés"));
    container.add(getCommentPanel(pendings));
  }

  private static JPanel getOneFilePanel(PatriLangFileContext file, ImageIcon icon) {
    var filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    filePanel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    filePanel.add(new JLabel(icon));
    filePanel.add(new JLabel(file.getBaseFileName()));

    return filePanel;
  }

  private static JPanel getCommentPanel(Collection<GroupedByComment> pendings) {
    var commentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    commentPanel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    commentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    commentPanel.add(new JLabel(getIcon(COMMENT_ICON_PATH)));
    commentPanel.add(
        new JLabel(String.format("%d - Nouveaux commentaires modifiés", pendings.size())));

    return commentPanel;
  }

  private static ImageIcon getIcon(String path) {
    var url = SyncConfirmDialog.class.getResource(path);
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
