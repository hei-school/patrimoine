package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static javax.swing.JOptionPane.showMessageDialog;
import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.model.Comment;

public class CommentSideBar extends JPanel {
  private String currentFileId;
  private final JFrame owner;
  private List<Comment> comments;
  private final ApiCache apiCache;
  private final CommentApi commentApi;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  private final JPanel listContainer;

  public CommentSideBar(JFrame owner, DriveApi driveApi) {
    super(new BorderLayout());

    this.owner = owner;
    this.comments = List.of();
    this.apiCache = ApiCache.getInstance();
    this.commentApi = new CommentApi(driveApi);

    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    var addCommentBtn = new Button("Ajouter un commentaire");
    topPanel.add(addCommentBtn, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);

    listContainer = new JPanel(new BorderLayout());
    add(listContainer, BorderLayout.CENTER);
  }

  private boolean hasSelectedFile() {
    return currentFileId != null && !currentFileId.isEmpty();
  }

  public JScrollPane toScrollPane() {
    return new JScrollPane(
        this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  }

  public void update(String currentFileId) {
    this.comments = List.of();
    this.currentFileId = currentFileId;

    SwingWorker<List<Comment>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Comment> doInBackground() throws Exception {
            if (!hasSelectedFile()) return List.of();
            return commentApi.getByFileId(currentFileId);
          }

          @Override
          protected void done() {
            try {
              comments = get();
              listContainer.removeAll();

              var contentPanel = new JPanel();
              contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
              contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
              contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

              if (comments.isEmpty()) {
                var label = new JLabel("Aucun commentaire pour ce fichier");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setBorder(new EmptyBorder(20, 20, 20, 20));
                contentPanel.add(label);
              } else {
                for (var comment : comments) {
                  var card = renderComment(comment);
                  var cardWrapper = new JPanel(new BorderLayout());
                  cardWrapper.setOpaque(false);
                  cardWrapper.add(card, BorderLayout.CENTER);
                  cardWrapper.setMaximumSize(
                      new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
                  contentPanel.add(cardWrapper);
                  contentPanel.add(Box.createVerticalStrut(10));
                }
              }

              var scroll =
                  new JScrollPane(
                      contentPanel,
                      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
              scroll.getVerticalScrollBar().setUnitIncrement(16);
              listContainer.add(scroll, BorderLayout.CENTER);

              revalidate();
              repaint();

            } catch (Exception e) {
              showMessageDialog(
                  owner,
                  "Erreur lors de get comment : " + e.getMessage(),
                  "Erreur",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };

    worker.execute();
  }

  private JPanel renderComment(Comment comment) {
    var card =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(222, 221, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
          }
        };

    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(new Color(245, 245, 245));
    card.setOpaque(false);
    card.setBorder(new EmptyBorder(10, 10, 10, 10));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);

    var dateStr = DATE_TIME_FORMATTER.format(comment.createdAt());
    var headerHtml =
        String.format(
            "<html><span style='font-size:16pt; font-weight:bold;'>%s</span><br>"
                + "<span style='font-size:14pt; color:gray;'>%s</span></html>",
            comment.author() != null ? comment.author().displayName() : "Inconnu", dateStr);

    var header = new JLabel(headerHtml);
    header.setBorder(new EmptyBorder(0, 0, 5, 0));
    header.setAlignmentX(Component.LEFT_ALIGNMENT);

    var contentText = comment.content() != null ? comment.content() : "";
    var content = new JLabel("<html>" + contentText.replaceAll("\n", "<br>") + "</html>");
    content.setBorder(new EmptyBorder(5, 5, 5, 5));
    content.setAlignmentX(Component.LEFT_ALIGNMENT);
    content.setFont(content.getFont().deriveFont(14f));

    var buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    buttons.setOpaque(false);
    buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
    buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
    var replyBtn = new Button("Répondre");
    buttons.add(replyBtn);

    if (!comment.replies().isEmpty()) {
      buttons.add(new Button("Réponses (" + comment.replies().size() + ")"));
    }

    var resolveBtn = new Button("Résoudre");
    buttons.add(resolveBtn);

    card.add(header);
    card.add(content);
    card.add(buttons);

    return card;
  }

  public void refetch() {
    if (hasSelectedFile()) {
      this.apiCache.invalidate(COMMENTS_CACHE_KEY, currentFileId);
    }
    this.update(currentFileId);
  }
}
