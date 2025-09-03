package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentCard extends JPanel {
  private final JFrame owner;
  private final Comment comment;
  private final int maxWidth;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  public CommentCard(JFrame owner, Comment comment, boolean showActions, int maxWidth) {
    this.owner = owner;
    this.comment = comment;
    this.maxWidth = maxWidth;

    setOpaque(false);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setBackground(new Color(245, 245, 245));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(10, 10, 10, 10));

    add(header());
    add(content());

    if (showActions) {
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
        var content = new JLabel("<html><div style='width:100%'>"
                + contentText.replaceAll("\n", "<br>") + "</div></html>");
        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.setFont(content.getFont().deriveFont(14f));
        content.setMaximumSize(new Dimension(maxWidth, Short.MAX_VALUE));

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

  private school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button replyBtn() {
    var replyBtn = new school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button("Répondre");
    replyBtn.addActionListener(e -> System.out.println("temp"));
    return replyBtn;
  }

  private school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button resolveBtn() {
    var resolveBtn = new school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button("Résoudre");
    resolveBtn.addActionListener(e -> System.out.println("temp"));
    return resolveBtn;
  }

  private school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button showRepliesBtn() {
    var showRepliesBtn = new school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button("Réponses (" + comment.replies().size() + ")");
    showRepliesBtn.addActionListener(e -> showRepliesDialog(comment));
    return showRepliesBtn;
  }

  private void showRepliesDialog(Comment parentComment) {
    var dialog = new JDialog(owner, "Réponses au commentaire", true);
    dialog.setLayout(new BorderLayout());
    dialog.setPreferredSize(new Dimension(800, 600));

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var mainWrapper = new CommentCard(owner, comment, false, 800).toWrappedCard();
    contentPanel.add(mainWrapper);
    contentPanel.add(Box.createVerticalStrut(15));

    var replies = parentComment.replies();
    if (replies.isEmpty()) {
      var label = new JLabel("Aucune réponse pour ce commentaire");
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      label.setBorder(new EmptyBorder(20, 20, 20, 20));
      contentPanel.add(label);
    } else {
      for (var reply : replies) {
        var wrapper = new CommentCard(owner, reply, false, 800).toWrappedCard();
        contentPanel.add(wrapper);
        contentPanel.add(Box.createVerticalStrut(10));
      }
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
    dialog.setLocationRelativeTo(owner);
    dialog.setVisible(true);
  }

  public JPanel toWrappedCard() {
    var wrapper = new JPanel(new BorderLayout());
    wrapper.setOpaque(false);
    wrapper.add(this, BorderLayout.CENTER);
    wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
    wrapper.setMaximumSize(new Dimension(maxWidth, Short.MAX_VALUE));
    return wrapper;
  }
}
