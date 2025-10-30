package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar.resolveComment;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentCard extends JPanel {
  private final Component parent;
  private final String fileId;
  private final Comment comment;
  private final Runnable refresh;
  private static final Color DEFAULT_BACKGROUND_COLOR = new Color(222, 221, 220);
  private static final Color APPROVED_BACKGROUND_COLOR = new Color(120, 220, 140);
  private static final Color RESOLVED_BACKGROUND_COLOR = new Color(255, 251, 156);
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  public CommentCard(
      Component parent, String fileId, Comment comment, boolean withActions, Runnable refresh) {
    this.fileId = fileId;
    this.comment = comment;
    this.refresh = refresh;
    this.parent = parent;

    setOpaque(false);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    var backgroundColor =
        comment.resolved()
            ? RESOLVED_BACKGROUND_COLOR
            : comment.isApproved() ? APPROVED_BACKGROUND_COLOR : DEFAULT_BACKGROUND_COLOR;
    setBackground(backgroundColor);

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
    g2.setColor(getBackground());
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

  public JTextArea content() {
    var contentText = comment.content() != null ? comment.content() : "";
    var content = new JTextArea(contentText);
    content.setLineWrap(true);
    content.setWrapStyleWord(true);
    content.setEditable(false);
    content.setFocusable(true);
    content.setOpaque(false);
    content.setFont(content.getFont().deriveFont(14f));
    content.setBorder(new EmptyBorder(5, 5, 5, 5));
    content.setAlignmentX(Component.LEFT_ALIGNMENT);

    int maxWidth = getMaximumSize().width;
    content.setSize(maxWidth, Short.MAX_VALUE);
    var d = content.getPreferredSize();
    content.setPreferredSize(new Dimension(maxWidth, d.height));
    content.setMaximumSize(new Dimension(maxWidth, d.height));
    return content;
  }

  public JPanel actions() {
    var buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    buttons.setOpaque(false);
    buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
    buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
    if (!comment.resolved()) {
      buttons.add(replyButton(fileId, comment, refresh));
    }

    if (!comment.answers().isEmpty()) {
      buttons.add(showAnswersButton(fileId, comment, refresh));
    }

    if (!comment.resolved()) {
      buttons.add(resolveButton(fileId, comment, refresh));
    }
    return buttons;
  }

  @Override
  public Dimension getMaximumSize() {
    var pref = getPreferredSize();
    return new Dimension(parent.getWidth() - 15, pref.height);
  }

  static Button showAnswersButton(String fileId, Comment parentComment, Runnable refresh) {
    return new Button(
        "Réponses (" + parentComment.answers().size() + ")",
        e -> new CommentAnswersDialog(fileId, parentComment, refresh));
  }

  static Button replyButton(String fileId, Comment parentComment, Runnable refresh) {
    return new Button("Répondre", e -> new CommentReplyDialog(fileId, parentComment, refresh));
  }

  static Button resolveButton(String fileId, Comment parentComment, Runnable refresh) {
    return new Button("Résolu", e -> resolveComment(fileId, parentComment, refresh));
  }
}
