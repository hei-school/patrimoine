package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.DatePicker;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.DeleteComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.ResolveComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.CommentsProvider;

public class CommentSideBar extends JPanel {
  private final State state;
  private DatePicker datePicker;
  private final CommentListPanel commentListPanel;

  public CommentSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.commentListPanel = new CommentListPanel(this, true, this::update);

    addTopPanel();
    addCommentList();
    addCommentFooter();

    state.subscribe(Set.of("pagination", "selectedFile") , this::update);
  }

  private static LocalDate getDefaultStartDate(){
    return LocalDate.now().minusMonths(3);
  }

  private void addTopPanel() {
    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    rightPanel.add(addCommentButton());

    datePicker = new DatePicker(getDefaultStartDate());
    datePicker.setPreferredSize(new Dimension(200, 35));
    datePicker.addActionListener(e -> update());
    rightPanel.add(datePicker);

    topPanel.add(rightPanel, BorderLayout.EAST);
    add(topPanel, BorderLayout.NORTH);
  }

  private Button addCommentButton() {
    String buttonLabel =
        "<html><table>"
            + "<tr>"
            + "<td style='padding:0; margin:0;'><span style='font-size:25pt;'>+</span></td>"
            + "<td style='padding:0; margin:0; padding-left:5'>Ajouter</td>"
            + "</tr>"
            + "</table></html>";

    var button = new Button(buttonLabel);
    button.setToolTipText("Ajouter un commentaire");
    button.setPreferredSize(new Dimension(110, 35));
    button.addActionListener(e -> new CommentAddDialog(() -> getSelectedFile(state), this::update));

    return button;
  }

  private void addCommentList() {
    add(commentListPanel.toScrollPane(), BorderLayout.CENTER);
  }

  private void addCommentFooter() {
    add(new CommentFooter(state), BorderLayout.SOUTH);
  }

  public void update() {
    var optionalSelectedFile = getSelectedFile(state);

    if(optionalSelectedFile.isEmpty()){
      commentListPanel.update(Optional::empty, List.of());
      return;
    }

    var selectedFile = optionalSelectedFile.get();
    Pagination pagination = state.get("pagination");
    Instant startDate = datePicker.getInstant();

    AsyncTask.<PaginatedResult<List<Comment>>>builder()
        .task(() -> CommentsProvider.getByFile(selectedFile, pagination, startDate))
        .withDialogLoading(false)
        .onSuccess(result -> commentListPanel.update(Optional::empty, result.data()))
        .onError(MessageDialog::showError)
        .build()
        .execute();
  }

  static void resolveComment(PatriLangFileContext file, Comment toResolve, Runnable onFinish) {
    if(isNotConfirmed("Voulez-vous vraiment marquer ce commentaire comme résolu ?")){
      return;
    }

    PendingCommentManager.add(new ResolveComment(file.getDriveId(), toResolve));
    onFinish.run();
  }

  static void removeComment(PatriLangFileContext file, Comment toDelete, Runnable onFinish) {
    if (!toDelete.getAuthor().me()) {
      showError("Vous ne pouvez supprimer que vos propres commentaires.");
      return;
    }

    if(isNotConfirmed("Voulez-vous vraiment supprimer ce commentaire ?")){
      return;
    }

    PendingCommentManager.add(new DeleteComment(file.getDriveId(), toDelete));
    onFinish.run();
  }

  private static boolean isNotConfirmed(String message){
    var confirm = showConfirmDialog(AppContext.getDefault().app(), message, "Confirmation", YES_NO_OPTION);
    return confirm != JOptionPane.YES_OPTION;
  }
}
