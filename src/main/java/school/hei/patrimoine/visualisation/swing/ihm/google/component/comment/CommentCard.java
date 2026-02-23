package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static java.awt.Color.BLACK;
import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar.removeComment;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar.resolveComment;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ChipPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.IconButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.SelectedFileSupplier;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter.DateFormatter;

public class CommentCard extends JPanel {
  private final Comment comment;
  private final Component parent;
  private final Runnable refresh;
  private final SelectedFileSupplier file;

  private static final Color DEFAULT_BACKGROUND_COLOR = new Color(222, 221, 220);
  private static final Color APPROVED_BACKGROUND_COLOR = new Color(120, 220, 140);
  private static final Color RESOLVED_BACKGROUND_COLOR = new Color(255, 251, 156);
  private static final Color RESOLVED_FONT_COLOR = APPROVED_BACKGROUND_COLOR;

  public CommentCard(
      SelectedFileSupplier file,
      Component parent,
      Comment comment,
      boolean withActions,
      Runnable refreshUI) {

    this.file = file;
    this.parent = parent;
    this.comment = comment;
    this.refresh = refreshUI;

    setOpaque(false);
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(getBackgroundColor(comment));

    add(header());
    add(content());

    if (withActions) {
      add(actions());
    }
  }

  private static Color getBackgroundColor(Comment comment) {
    if (comment.isResolved()) {
      return RESOLVED_BACKGROUND_COLOR;
    }
    if (comment.isApproved()) {
      return APPROVED_BACKGROUND_COLOR;
    }
    return DEFAULT_BACKGROUND_COLOR;
  }

  @Override
  protected void paintComponent(Graphics g) {
    var g2 = (Graphics2D) g.create();
    g2.setColor(getBackground());
    g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
    g2.dispose();
    super.paintComponent(g);
  }

  public JPanel header() {
    var headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    var headerLabel = headerLabel();
    headerPanel.add(headerLabel, BorderLayout.WEST);

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    rightPanel.setOpaque(false);

    if (comment.isResolved()) {
      rightPanel.add(new ChipPanel("Résolu", RESOLVED_FONT_COLOR, BLACK));
    }

    if (comment.getAuthor().me()) {
      rightPanel.add(removeButton(file, comment, refresh));
    }

    headerPanel.add(rightPanel, BorderLayout.EAST);
    return headerPanel;
  }

  public JLabel headerLabel() {
    var createdStr = DateFormatter.format(comment.getCreatedAt());
    var lastModified = comment.getLastModifiedDate();

    var modifiedStr = "";
    if (lastModified != null && !lastModified.equals(comment.getCreatedAt())) {
      modifiedStr = " (modifié le " + DateFormatter.format(lastModified) + ")";
    }

    var headerHtml =
        String.format(
            "<html><span style='font-size:16pt; font-weight:bold;'>%s</span><br>"
                + "<span style='font-size:14pt; color:gray;'>%s%s</span></html>",
            comment.getAuthor() != null ? comment.getAuthor().displayName() : "Inconnu",
            createdStr,
            modifiedStr);

    var header = new JLabel(headerHtml);
    header.setBorder(new EmptyBorder(0, 0, 5, 0));
    header.setAlignmentX(Component.LEFT_ALIGNMENT);

    return header;
  }

  public JTextArea content() {
    var content = new JTextArea(comment.getContent());
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

    if (!comment.isResolved()) {
      buttons.add(replyButton(file, comment, refresh));
    }

    if (!comment.getAnswers().isEmpty()) {
      buttons.add(showAnswersButton(file, comment, parent, refresh));
    }

    if (!comment.isResolved()) {
      buttons.add(resolveButton(file, comment, refresh));
    }
    return buttons;
  }

  @Override
  public Dimension getMaximumSize() {
    var pref = getPreferredSize();
    return new Dimension(parent.getWidth() - 15, pref.height);
  }

  static Button showAnswersButton(
      SelectedFileSupplier file, Comment comment, Component parent, Runnable refresh) {
    return new Button(
        "Réponses (" + comment.getAnswers().size() + ")",
        e -> new CommentAnswersDialog(file, comment, parent, refresh));
  }

  static Button replyButton(SelectedFileSupplier file, Comment parentComment, Runnable refresh) {
    return new Button("Répondre", e -> new CommentReplyDialog(file, parentComment, refresh));
  }

  static Button resolveButton(SelectedFileSupplier file, Comment comment, Runnable refresh) {
    return new Button("Résoudre", e -> resolveComment(file.get().orElseThrow(), comment, refresh));
  }

  static IconButton removeButton(SelectedFileSupplier file, Comment comment, Runnable refresh) {
    var button = new IconButton(loadRemoveIcon(), 18, "Supprimer le commentaire");
    button.setAlignmentY(Component.CENTER_ALIGNMENT);
    button.addActionListener(e -> removeComment(file.get().orElseThrow(), comment, refresh));
    return button;
  }

  private static Image loadRemoveIcon() {
    try {
      var removeIcon =
          ImageIO.read(requireNonNull(CommentCard.class.getResource("/icons/remove.png")));
      return removeIcon.getScaledInstance(18, 18, SCALE_SMOOTH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
