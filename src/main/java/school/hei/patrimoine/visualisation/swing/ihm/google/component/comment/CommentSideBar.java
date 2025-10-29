package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showInfo;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.DatePicker;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class CommentSideBar extends JPanel {
  private final State state;
  private final ApiCache apiCache;
  private final CommentApi commentApi;
  private final CommentListPanel commentListPanel;
  private final CommentFooter footer;
  private DatePicker datePicker;

  private final Map<String, Pagination> paginationByFile = new HashMap<>();
  private final Map<String, List<String>> previousTokensByFile = new HashMap<>();

  public CommentSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.apiCache = ApiCache.getInstance();
    this.commentApi = AppContext.getDefault().getData("comment-api");
    this.commentListPanel = new CommentListPanel(this, true, this::refreshCurrentFileCommentsCache);
    this.footer = new CommentFooter(this::goToPreviousPage, this::goToNextPage);

    addTopPanel();
    addCommentList();
    addCommentFooter();

    state.subscribe(Set.of("selectedFile", "selectedFileId"), this::update);
  }

  private void addTopPanel() {
    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

    var addCommentBtn =
        new Button(
            "Ajouter un commentaire",
            e -> new CommentAddDialog(state, this::refreshCurrentFileCommentsCache));
    rightPanel.add(addCommentBtn);

    this.datePicker = new DatePicker(LocalDate.now().minusMonths(3));
    this.datePicker.setPreferredSize(new Dimension(200, 35));
    this.datePicker.addActionListener(e -> update());
    rightPanel.add(datePicker);

    topPanel.add(rightPanel, BorderLayout.EAST);
    add(topPanel, BorderLayout.NORTH);
  }

  public Instant datePickerToInstant(DatePicker startDatePicker) {
    LocalDate date =
        LocalDate.of(
            startDatePicker.getModel().getYear(),
            startDatePicker.getModel().getMonth() + 1,
            startDatePicker.getModel().getDay());

    return date.atStartOfDay(ZoneId.of("Indian/Antananarivo")).toInstant();
  }

  private void addCommentList() {
    add(commentListPanel.toScrollPane(), BorderLayout.CENTER);
  }

  private void addCommentFooter() {
      add(footer, BorderLayout.SOUTH);
  }

  private void update() {
    String fileId = state.get("selectedFileId");
    if (fileId == null) return;

    resetPaginationForCurrentFile(fileId);
    loadComments(fileId, datePickerToInstant(datePicker));
  }

  private void loadComments(String fileId, Instant startDate) {
    if (fileId == null) return;

    initFilePagination(fileId);

    Pagination filePagination = paginationByFile.get(fileId);
    List<String> fileTokens = previousTokensByFile.get(fileId);

    AsyncTask.<PaginatedResult<List<Comment>>>builder()
        .task(() -> commentApi.getByFileId(fileId, filePagination, startDate))
        .onSuccess(
            result -> {
              commentListPanel.update(fileId, result.data());

              paginationByFile.put(fileId, result.getNextPagination());

              previousTokensByFile.put(fileId, fileTokens);

              footer.updateButtons(
                  fileTokens.size() > 1, result.getNextPagination().pageToken() != null);
            })
        .withDialogLoading(false)
        .onError(e -> showError("Error", "Erreur lors de la récupération des commentaires"))
        .build()
        .execute();
  }

  private void initFilePagination(String fileId) {
    paginationByFile.putIfAbsent(fileId, new Pagination(5, null));
    previousTokensByFile.putIfAbsent(fileId, new ArrayList<>(List.of("firstPage")));
  }

  private void resetPaginationForCurrentFile(String fileId) {
    paginationByFile.put(fileId, new Pagination(5, null));
    previousTokensByFile.put(fileId, new ArrayList<>(List.of("firstPage")));
  }

  private void goToNextPage() {
    String fileId = state.get("selectedFileId");
    if (fileId == null) return;

    initFilePagination(fileId);
    Pagination filePagination = paginationByFile.get(fileId);
    List<String> fileTokens = previousTokensByFile.get(fileId);

    if (filePagination.pageToken() != null) {
      String token = filePagination.pageToken();
      if (!fileTokens.contains(token)) {
        fileTokens.add(token);
      }
      loadComments(fileId, datePickerToInstant(datePicker));
    }
  }

  private void goToPreviousPage() {
    String fileId = state.get("selectedFileId");
    if (fileId == null) return;

    initFilePagination(fileId);
    List<String> fileTokens = previousTokensByFile.get(fileId);

    if (fileTokens.size() > 1) {
      fileTokens.removeLast();
      String previousToken = fileTokens.getLast();
      paginationByFile.put(
          fileId, new Pagination(5, previousToken.equals("firstPage") ? null : previousToken));
      loadComments(fileId, datePickerToInstant(datePicker));
    }
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
