package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.*;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingCommentsInfo;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.NamedID;

public class SyncConfirmDialog extends JDialog {
  @Getter private boolean confirmed = false;

  private static final String FILE_ICON_PATH = "/icons/file.png";
  private static final String COMMENT_ICON_PATH = "/icons/comment.png";

  public SyncConfirmDialog(DialogMode mode) {
    super((Frame) null, mode.title, true);

    List<File> plannedFiles = getStagedPlannedFiles();
    List<File> doneFiles = getStagedDoneFiles();
    List<File> justificativeFiles = getStagedJustificativeFiles();

    var localCommentManager = LocalCommentManager.getInstance();
    List<String> filesWithPendingComments = localCommentManager.getPendingFileIds();
    Map<String, PendingCommentsInfo> pendingComments = new HashMap<>();

    for (var fileId : filesWithPendingComments) {
      collectPendingComments(fileId, localCommentManager, pendingComments);
    }

    var hasFiles = !plannedFiles.isEmpty() || !doneFiles.isEmpty() || !justificativeFiles.isEmpty();
    var hasComments = !pendingComments.isEmpty();

    if (hasFiles || hasComments) {
      initConfirmComponents(
          plannedFiles,
          doneFiles,
          justificativeFiles,
          pendingComments,
          mode.message,
          mode.confirmButtonText);
    } else {
      initInfoComponents();
    }

    pack();
    setLocationRelativeTo(null);
  }

  private static void collectPendingComments(
      String fileId,
      LocalCommentManager localCommentManager,
      Map<String, PendingCommentsInfo> pendingComments) {
    int addedComments = localCommentManager.getPendingComments(fileId).size();
    int repliedComments = localCommentManager.getPendingReplies(fileId).size();
    int resolvedComments = localCommentManager.getPendingResolutions(fileId).size();
    int deletedComments = localCommentManager.getPendingDeletions(fileId).size();

    if (addedComments == 0 && repliedComments == 0 && resolvedComments == 0 && deletedComments == 0)
      return;

    var info =
        PendingCommentsInfo.builder()
            .fileId(fileId)
            .addCount(addedComments)
            .replyCount(repliedComments)
            .resolveCount(resolvedComments)
            .deleteCount(deletedComments)
            .build();

    pendingComments.put(fileId, info);
  }

  private void initConfirmComponents(
      List<File> plannedFiles,
      List<File> doneFiles,
      List<File> justificativeFiles,
      Map<String, PendingCommentsInfo> pendingComments,
      String messageText,
      String confirmButtonText) {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    if (!plannedFiles.isEmpty() || !doneFiles.isEmpty() || !justificativeFiles.isEmpty()) {
      var filesLabel = new JLabel("Fichiers modifiés");
      filesLabel.setFont(new Font("Arial", Font.BOLD, 16));
      filesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      filesLabel.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 8, 0).build());
      contentPanel.add(filesLabel);

      addFileSection(contentPanel, "Planifiés", plannedFiles, loadIcon(FILE_ICON_PATH));
      addFileSection(contentPanel, "Réalisés", doneFiles, loadIcon(FILE_ICON_PATH));
      addFileSection(
          contentPanel, "Pièces justificatives", justificativeFiles, loadIcon(FILE_ICON_PATH));

      if (hasPendingComments(pendingComments)) {
        contentPanel.add(Box.createVerticalStrut(15));
      }
    }

    if (hasPendingComments(pendingComments)) {
      addCommentsSection(contentPanel, pendingComments, loadIcon(COMMENT_ICON_PATH));
    }

    var scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(CustomBorder.builder().thickness(0).padding(0, 0).build());
    int computedHeight = Math.min(contentPanel.getPreferredSize().height, 600);
    scrollPane.setPreferredSize(new Dimension(500, computedHeight));

    var messagePanel = createMessagePanel(scrollPane, messageText);
    add(messagePanel, BorderLayout.CENTER);

    addButtons(confirmButtonText);
  }

  private JPanel createMessagePanel(JComponent centerComponent, String messageText) {
    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 15).build());
    messagePanel.add(centerComponent, BorderLayout.CENTER);

    var questionLabel = new JLabel(messageText);
    questionLabel.setBorder(CustomBorder.builder().thickness(0).padding(10, 0, 0, 0).build());

    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    return messagePanel;
  }

  private boolean hasPendingComments(Map<String, PendingCommentsInfo> pendingComments) {
    return !pendingComments.isEmpty();
  }

  private void initInfoComponents() {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);

    Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");
    var iconLabel = new JLabel(infoIcon);

    var messageLabel = new JLabel("Aucune modification en attente de synchronisation.");
    messageLabel.setFont(UIManager.getFont("Label.font"));

    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 10).build());
    messagePanel.add(iconLabel, BorderLayout.WEST);
    messagePanel.add(messageLabel, BorderLayout.CENTER);

    add(messagePanel, BorderLayout.CENTER);


    var okButton = new Button("OK");
    okButton.setPreferredSize(new Dimension(70, okButton.getPreferredSize().height));
    okButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(okButton);

    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(5, 15, 15, 15).build());
    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);

    setMinimumSize(new Dimension(300, 100));
  }

  private void addButtons(String confirmButtonText) {
    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(5, 15, 15, 15).build());

    var cancelButton = new Button("Annuler");
    cancelButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    var confirmButton = new Button(confirmButtonText);
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

    for (var file : files) {
      container.add(createFilePanel(file, icon));
      container.add(Box.createVerticalStrut(5));
    }

    container.add(Box.createVerticalStrut(10));
  }

  private void addCommentsSection(
      JPanel container, Map<String, PendingCommentsInfo> pendingCommentsMap, ImageIcon icon) {
    if (pendingCommentsMap.isEmpty()) return;

    var label = new JLabel("Commentaires modifiés");
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
      GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");

      var plannedName =
          ids.planned().stream().filter(n -> n.id().equals(fileId)).map(NamedID::name).findFirst();

      if (plannedName.isPresent()) {
        return plannedName.get();
      }

      var doneName =
          ids.done().stream().filter(n -> n.id().equals(fileId)).map(NamedID::name).findFirst();

      if (doneName.isPresent()) {
        return doneName.get();
      }

      var justificativeName =
          ids.justificative().stream()
              .filter(n -> n.id().equals(fileId))
              .map(NamedID::name)
              .findFirst();

      if (justificativeName.isPresent()) {
        return justificativeName.get();
      }
    } catch (Exception ignored) {
    }

    return "Fichier " + fileId.substring(0, Math.min(8, fileId.length())) + "...";
  }

  private ImageIcon loadIcon(String resourcePath) {
    var url = getClass().getResource(resourcePath);
    var icon = new ImageIcon(requireNonNull(url));
    Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

    return new ImageIcon(scaled);
  }
}
