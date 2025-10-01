package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.showError;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.showInfo;

import java.awt.*;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

public class CommentSideBar extends JPanel {
  private final State state;
  private final ApiCache apiCache;
  private final CommentApi commentApi;
  private final CommentListPanel commentListPanel;

  public CommentSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.apiCache = ApiCache.getInstance();
    this.commentApi = AppContext.getDefault().getData("comment-api");
    this.commentListPanel = new CommentListPanel(this, true, this::refreshCurrentFileCommentsCache);

    addTopPanel();
    addCommentList();

    state.subscribe(Set.of("selectedFile", "selectedFileId"), this::update);
  }

  private void addTopPanel() {
    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    var addCommentBtn =
        new Button(
            "Ajouter un commentaire",
            e -> new CommentAddDialog(state, this::refreshCurrentFileCommentsCache));
    topPanel.add(addCommentBtn, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);
  }

  private void addCommentList() {
    add(commentListPanel.toScrollPane(), BorderLayout.CENTER);
  }

  private void update() {
    AsyncTask.<List<Comment>>builder()
        .task(
            () -> {
              if (state.get("selectedFile") == null) {
                return List.of();
              }

              var paginatedResult =
                  commentApi.getByFileId(state.get("selectedFileId"), new Pagination(100, null));
              return paginatedResult.data();
            })
        .onSuccess(newComments -> commentListPanel.update(state.get("selectedFileId"), newComments))
        .withDialogLoading(false)
        .onError(e -> showError("Error", "Erreur lors de la récupération des commentaires"))
        .build()
        .execute();
  }

  private void refreshCurrentFileCommentsCache() {
    if (state.get("selectedFile") != null) {
      this.apiCache.invalidate(
          COMMENTS_CACHE_KEY, cacheKey -> cacheKey.startsWith(state.get("selectedFileId")));
    }

    this.update();
  }

  static void resolveComment(String fileId, Comment toResolve, Runnable refresh) {
    int confirm =
        JOptionPane.showConfirmDialog(
            AppContext.getDefault().app(),
            "Voulez-vous vraiment marquer ce commentaire comme résolu ?",
            "Résolution de commentaire",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }

    CommentApi commentApi = AppContext.getDefault().getData("comment-api");
    AsyncTask.<Void>builder()
        .task(
            () -> {
              commentApi.resolve(fileId, toResolve);
              return null;
            })
        .loadingMessage("Envoi en cours...")
        .onSuccess(
            result -> {
              showInfo("Succès", "Le commentaire a été envoyé avec succès.");
              refresh.run();
            })
        .onError(
            error ->
                showError("Erreur", "Impossible d'envoyer le commentaire. Veuillez réessayer."))
        .build()
        .execute();
  }
}
