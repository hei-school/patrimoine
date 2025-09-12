package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;

import java.awt.*;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog;

public class CommentSideBar extends JPanel {
  private final State state;
  private final ApiCache apiCache;
  private final CommentApi commentApi;
  private final JPanel listContainer;

  public CommentSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.apiCache = ApiCache.getInstance();
    this.commentApi = AppContext.getDefault().getData("comment-api");

    addTopPanel();
    listContainer = new JPanel(new BorderLayout());
    add(listContainer, BorderLayout.CENTER);

    state.subscribe(Set.of("selectedFile", "selectedFileId"), this::update);
  }

  private void addTopPanel() {
    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    var addCommentBtn = new Button("Ajouter un commentaire");
    topPanel.add(addCommentBtn, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);
  }

  public JScrollPane toScrollPane() {
    return new JScrollPane(
        this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  }

  public void update() {
    AsyncTask.<List<Comment>>builder()
        .task(this::getCurrentFileComments)
        .withDialogLoading(false)
        .onError(
            e -> MessageDialog.error("Error", "Erreur lors de la récupération des commentaires"))
        .onSuccess(this::drawComments)
        .build()
        .execute();
  }

  private void replyComment(Comment toReply, String newCommentContent) {
    AsyncTask.<Void>builder()
        .task(
            () -> {
              commentApi.reply(state.get("selectedFileId"), toReply.id(), newCommentContent);
              return null;
            })
        .loadingMessage("Envoi en cours...")
        .onSuccess(result -> refreshCurrentFileCommentsCache())
        .onError(error -> MessageDialog.error("Error", "Erreur lors de l'envoi du commentaire"))
        .build()
        .execute();
  }

  private void resolveComment(Comment toResolve) {
    AsyncTask.<Void>builder()
        .task(
            () -> {
              commentApi.resolve(state.get("selectedFileId"), toResolve.id());
              return null;
            })
        .loadingMessage("Envoi en cours...")
        .onSuccess(result -> refreshCurrentFileCommentsCache())
        .onError(error -> MessageDialog.error("Error", "Erreur lors de l'envoi du commentaire"))
        .build()
        .execute();
  }

  private List<Comment> getCurrentFileComments() throws GoogleIntegrationException {
    if (state.get("selectedFile") == null) {
      return List.of();
    }

    return commentApi.getByFileId(state.get("selectedFileId"));
  }

  private void refreshCurrentFileCommentsCache() {
    if (state.get("selectedFile") != null) {
      this.apiCache.invalidate(COMMENTS_CACHE_KEY, state.get("selectedFileId"));
    }

    this.update();
  }

  private void drawComments(List<Comment> comments) {
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
        var commentCard = new CommentCard(state.get("selectedFileId"), comment, null, false);
        contentPanel.add(commentCard);
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
  }
}
