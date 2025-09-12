package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentCard extends JPanel {
  private final String fileId;
  private final Comment comment;
  private final BiConsumer<Comment, String> sendReplyConsumer;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  public CommentCard(
      String fileId,
      Comment comment,
      BiConsumer<Comment, String> sendReplyConsumer,
      boolean withActions) {
    this.fileId = fileId;
    this.comment = comment;
    this.sendReplyConsumer = sendReplyConsumer;

    setOpaque(false);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setBackground(new Color(245, 245, 245));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    add(header());
    add(content());

    if (withActions) {
      add(actions());
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    var g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(new Color(222, 221, 220));
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
    g2.dispose();
    super.paintComponent(g);
  }

  public JLabel header() {
    var dateStr = DATE_TIME_FORMATTER.format(comment.createdAt());
    var headerHtml =
        String.format(
            "<html><span style='font-size:16pt; font-weight:bold;'>%s</span><br>"
                + "<span style='font-size:14pt; color:gray;'>%s</span></html>",
            comment.author() != null ? comment.author().displayName() : "Inconnu", dateStr);

    var header = new JLabel(headerHtml);
    header.setBorder(new EmptyBorder(0, 0, 5, 0));
    header.setAlignmentX(Component.LEFT_ALIGNMENT);

    return header;
  }

  public JLabel content() {
    var contentText = comment.content() != null ? comment.content() : "";
    var content =
        new JLabel(
            "<html><div style='width:100%'>"
                + contentText.replaceAll("\n", "<br>")
                + "</div></html>");
    content.setBorder(new EmptyBorder(5, 5, 5, 5));
    content.setAlignmentX(Component.LEFT_ALIGNMENT);
    content.setFont(content.getFont().deriveFont(14f));

    return content;
  }

  public JPanel actions() {
    var buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    buttons.setOpaque(false);
    buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
    buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
    buttons.add(replyBtn());

    if (!comment.replies().isEmpty()) {
      buttons.add(showRepliesBtn());
    }

    buttons.add(resolveBtn());
    return buttons;
  }

  private Button replyBtn() {
    var replyBtn = new Button("Répondre");
    replyBtn.addActionListener(e -> showReplyDialog());
    return replyBtn;
  }

  private Button resolveBtn() {
    var resolveBtn = new Button("Résoudre");
    resolveBtn.addActionListener(e -> showReplyDialog());
    return resolveBtn;
  }

  private Button showRepliesBtn() {
    var showRepliesBtn = new Button("Réponses (" + comment.replies().size() + ")");
    showRepliesBtn.addActionListener(e -> showAnswerssDialog(comment));
    return showRepliesBtn;
  }

  private void showAnswerssDialog(Comment parentComment) {
    var dialog =
        new school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog(
            "Réponses au commentaire", 800, 600, false);

    dialog.setModal(true);
    dialog.setLayout(new BorderLayout());

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var mainWrapper = new CommentCard(fileId, comment, sendReplyConsumer, false);
    contentPanel.add(mainWrapper);
    contentPanel.add(Box.createVerticalStrut(15));

    for (var reply : parentComment.replies()) {
      var commentCard = new CommentCard(fileId, reply, sendReplyConsumer, false);
      contentPanel.add(commentCard);
      contentPanel.add(Box.createVerticalStrut(10));
    }

    var scroll =
        new JScrollPane(
            contentPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    dialog.add(scroll, BorderLayout.CENTER);

    var closeBtn = new Button("Fermer");
    closeBtn.addActionListener(e -> dialog.dispose());

    var btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnPanel.add(replyBtn());
    btnPanel.add(resolveBtn());
    btnPanel.add(closeBtn);

    dialog.add(btnPanel, BorderLayout.SOUTH);
    dialog.pack();
    dialog.setLocationRelativeTo(AppContext.getDefault().app());
    dialog.setVisible(true);
  }

  private void showReplyDialog() {
    var dialog = new Dialog("Répondre au commentaire", 500, 200, false);
    dialog.setLayout(new BorderLayout());

    var textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

    var sendBtn = new Button("Envoyer");
    sendBtn.addActionListener(
        e -> {
          var content = textArea.getText().trim();
          if (!content.isEmpty()) {
            sendReplyConsumer.accept(comment, content);
            dialog.dispose();
          }
        });

    var cancelBtn = new Button("Annuler");
    cancelBtn.addActionListener(e -> dialog.dispose());

    var btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnPanel.add(sendBtn);
    btnPanel.add(cancelBtn);
    dialog.add(btnPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(AppContext.getDefault().app());
    dialog.setPreferredSize(new Dimension(750, 450));
  }
}
