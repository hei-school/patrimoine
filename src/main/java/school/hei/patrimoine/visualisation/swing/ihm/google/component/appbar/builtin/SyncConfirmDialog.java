package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.*;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingCommentsInfo;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;

public class SyncConfirmDialog extends JDialog {
  @Getter private boolean confirmed = false;

  public SyncConfirmDialog(boolean isQuitDialog) {
    super(
        (Frame) null,
        isQuitDialog ? "Modifications non synchronisées" : "Confirmer la synchronisation",
        true);

    List<File> plannedFiles = getStagedPlannedFiles();
    List<File> doneFiles = getStagedDoneFiles();
    List<File> justificativeFiles = getStagedJustificativeFiles();

    LocalCommentManager localManager = LocalCommentManager.getInstance();
    List<String> filesWithPendingComments = localManager.getFilesWithPendingChanges();
    Map<String, PendingCommentsInfo> pendingCommentsPlanned = new HashMap<>();
    Map<String, PendingCommentsInfo> pendingCommentsDone = new HashMap<>();
    Map<String, PendingCommentsInfo> pendingJustificativeComments = new HashMap<>();

    for (String fileId : filesWithPendingComments) {
      int addCount = localManager.getPendingComments(fileId).size();
      int replyCount = localManager.getPendingReplies(fileId).size();
      int resolveCount = localManager.getPendingResolutions(fileId).size();
      int deleteCount = localManager.getPendingDeletions(fileId).size();

      if (addCount > 0 || replyCount > 0 || resolveCount > 0 || deleteCount > 0) {
        var info = new PendingCommentsInfo(fileId, addCount, replyCount, resolveCount, deleteCount);

        if (isJustificative(fileId)) {
          pendingJustificativeComments.put(fileId, info);
        } else if (isPlannedFile(fileId)) {
          pendingCommentsPlanned.put(fileId, info);
        } else {
          pendingCommentsDone.put(fileId, info);
        }
      }
    }

    boolean hasFiles =
        !plannedFiles.isEmpty() || !doneFiles.isEmpty() || !justificativeFiles.isEmpty();
    boolean hasComments =
        !pendingCommentsPlanned.isEmpty()
            || !pendingCommentsDone.isEmpty()
            || !pendingJustificativeComments.isEmpty();

    if (hasFiles || hasComments) {
      String messageText =
          isQuitDialog
              ? "Il reste des modifications non synchronisées. Voulez-vous vraiment quitter ?"
              : "Voulez-vous synchroniser ces modifications avec Google Drive ?";

      String confirmButtonText = isQuitDialog ? "Quitter" : "Synchroniser";
      initConfirmComponents(
          plannedFiles,
          doneFiles,
          justificativeFiles,
          pendingCommentsPlanned,
          pendingCommentsDone,
          pendingJustificativeComments,
          messageText,
          confirmButtonText);
    } else {
      initInfoComponents();
    }

    pack();
    setLocationRelativeTo(null);
  }

  private boolean isPlannedFile(String fileId) {
    try {
      GoogleLinkList<GoogleLinkList.NamedID> ids = AppContext.getDefault().getData("named-ids");

      return ids.planned().stream().anyMatch(n -> n.id().equals(fileId));
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isJustificative(String fileId) {
    try {
      GoogleLinkList<GoogleLinkList.NamedID> ids = AppContext.getDefault().getData("named-ids");

      return ids.justificative().stream().anyMatch(n -> n.id().equals(fileId));
    } catch (Exception e) {
      return false;
    }
  }

  private void initConfirmComponents(
      List<File> plannedFiles,
      List<File> doneFiles,
      List<File> justificativeFiles,
      Map<String, PendingCommentsInfo> pendingCommentsPlanned,
      Map<String, PendingCommentsInfo> pendingCommentsDone,
      Map<String, PendingCommentsInfo> pendingJustificativeComments,
      String messageText,
      String confirmButtonText) {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 15).build());
    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    if (!plannedFiles.isEmpty() || !doneFiles.isEmpty() || !justificativeFiles.isEmpty()) {
      var filesLabel = new JLabel("Fichiers modifiés");
      filesLabel.setFont(new Font("Arial", Font.BOLD, 16));
      filesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      filesLabel.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 8, 0).build());
      contentPanel.add(filesLabel);

      addFileSection(contentPanel, "Planifiés", plannedFiles, loadFileIcon());
      addFileSection(contentPanel, "Réalisés", doneFiles, loadFileIcon());
      addFileSection(contentPanel, "Pièces justificatives", justificativeFiles, loadFileIcon());

      if (!pendingCommentsPlanned.isEmpty()
          || !pendingCommentsDone.isEmpty()
          || !pendingJustificativeComments.isEmpty()) {
        contentPanel.add(Box.createVerticalStrut(15));
      }
    }

    if (!pendingCommentsPlanned.isEmpty()
        || !pendingCommentsDone.isEmpty()
        || !pendingJustificativeComments.isEmpty()) {
      var commentsLabel = new JLabel("Commentaires modifiés");
      commentsLabel.setFont(new Font("Arial", Font.BOLD, 16));
      commentsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      commentsLabel.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 8, 0).build());
      contentPanel.add(commentsLabel);

      addCommentsSection(contentPanel, "Planifiés", pendingCommentsPlanned, loadCommentIcon());
      addCommentsSection(contentPanel, "Réalisés", pendingCommentsDone, loadCommentIcon());
      addCommentsSection(
          contentPanel, "pièces justificatives", pendingJustificativeComments, loadCommentIcon());
    }

    var scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(CustomBorder.builder().thickness(0).padding(0, 0).build());
    int maxHeight = 600;
    int computedHeight = Math.min(contentPanel.getPreferredSize().height, maxHeight);
    scrollPane.setPreferredSize(new Dimension(500, computedHeight));

    messagePanel.add(scrollPane, BorderLayout.CENTER);

    var questionLabel = new JLabel(messageText);
    questionLabel.setBorder(CustomBorder.builder().thickness(0).padding(10, 0, 0, 0).build());

    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    add(messagePanel, BorderLayout.CENTER);

    addButtons(confirmButtonText);
  }

  private void initInfoComponents() {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 10).build());

    Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");
    var iconLabel = new JLabel(infoIcon);

    var messageLabel = new JLabel("Aucune modification en attente de synchronisation.");
    messageLabel.setFont(UIManager.getFont("Label.font"));

    messagePanel.add(iconLabel, BorderLayout.WEST);
    messagePanel.add(messageLabel, BorderLayout.CENTER);

    add(messagePanel, BorderLayout.CENTER);

    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(5, 15, 15, 15).build());

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

  private void addButtons(String confirmButtonText) {
    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(5, 15, 15, 15).build());

    var cancelButton = new JButton("Annuler");
    cancelButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    var confirmButton = new JButton(confirmButtonText);
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
    label.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 3, 0).build());

    container.add(label);

    for (File file : files) {
      container.add(createFilePanel(file, icon));
      container.add(Box.createVerticalStrut(5));
    }

    container.add(Box.createVerticalStrut(10));
  }

  private void addCommentsSection(
      JPanel container,
      String title,
      Map<String, PendingCommentsInfo> pendingCommentsMap,
      ImageIcon icon) {
    if (pendingCommentsMap.isEmpty()) return;

    var label = new JLabel(title);
    label.setFont(new Font("Arial", Font.BOLD, 15));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    label.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 3, 0).build());

    container.add(label);

    for (Map.Entry<String, PendingCommentsInfo> entry : pendingCommentsMap.entrySet()) {
      PendingCommentsInfo info = entry.getValue();
      container.add(createCommentPanel(info, icon));
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

  private JPanel createCommentPanel(PendingCommentsInfo info, ImageIcon icon) {
    var commentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    commentPanel.setBorder(
        CustomBorder.builder()
            .radius(20)
            .borderColor(new Color(180, 180, 180))
            .padding(10, 0)
            .thickness(1)
            .build());
    commentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    var iconLabel = new JLabel(icon);
    commentPanel.add(iconLabel);

    var nameLabel = new JLabel(buildCommentDetails(info));
    nameLabel.setFont(UIManager.getFont("Label.font"));

    commentPanel.add(nameLabel);

    return commentPanel;
  }

  private String buildCommentDetails(PendingCommentsInfo info) {
    StringBuilder details = new StringBuilder();
    details.append(getFileNameFromId(info.fileId())).append(" : ");

    appendCount(details, info.addCount(), "ajout");
    appendCount(details, info.replyCount(), "réponse");
    appendCount(details, info.resolveCount(), "résolution");
    appendCount(details, info.deleteCount(), "suppression");

    return details.toString();
  }

  private void appendCount(StringBuilder sb, int count, String label) {
    if (count <= 0) return;

    if (sb.charAt(sb.length() - 2) != ':') {
      sb.append(", ");
    }

    sb.append(count).append(" ").append(label);
    if (count > 1) sb.append("s");
  }

  private String getFileNameFromId(String fileId) {
    try {
      GoogleLinkList<GoogleLinkList.NamedID> ids = AppContext.getDefault().getData("named-ids");

      var plannedName =
          ids.planned().stream()
              .filter(n -> n.id().equals(fileId))
              .map(GoogleLinkList.NamedID::name)
              .findFirst();

      if (plannedName.isPresent()) {
        return plannedName.get();
      }

      var doneName =
          ids.done().stream()
              .filter(n -> n.id().equals(fileId))
              .map(GoogleLinkList.NamedID::name)
              .findFirst();

      if (doneName.isPresent()) {
        return doneName.get();
      }

      var justificativeName =
          ids.justificative().stream()
              .filter(n -> n.id().equals(fileId))
              .map(GoogleLinkList.NamedID::name)
              .findFirst();

      if (justificativeName.isPresent()) {
        return justificativeName.get();
      }
    } catch (Exception ignored) {
    }

    return "Fichier " + fileId.substring(0, Math.min(8, fileId.length())) + "...";
  }

  private ImageIcon loadFileIcon() {
    var url = getClass().getResource("/icons/file.png");
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }

  private ImageIcon loadCommentIcon() {
    var url = getClass().getResource("/icons/comment.png");
    var icon = new ImageIcon(Objects.requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }

  public static boolean forSync() {
    var dialog = new SyncConfirmDialog(false);
    dialog.setVisible(true);
    return dialog.isConfirmed();
  }

  public static boolean forQuit() {
    var dialog = new SyncConfirmDialog(true);
    dialog.setVisible(true);
    return dialog.isConfirmed();
  }
}
