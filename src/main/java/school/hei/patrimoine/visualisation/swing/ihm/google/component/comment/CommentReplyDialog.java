package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.SelectedFileSupplier;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.ReplyComment;

public class CommentReplyDialog extends Dialog {
  private final Comment comment;
  private final Runnable refresh;
  private final JTextArea textArea;
  private final SelectedFileSupplier file;

  public CommentReplyDialog(SelectedFileSupplier file, Comment comment, Runnable refresh) {
    super("Répondre au commentaire", 500, 300, false);

    this.file = file;
    this.refresh = refresh;
    this.comment = comment;
    this.textArea = new JTextArea();

    setModal(true);
    setLayout(new BorderLayout());

    addTitle();
    addTextArea();
    addActions();

    pack();
    setVisible(true);
  }

  private void addTitle() {
    var title = new JLabel("Ajouter un commentaire :");
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(title, BorderLayout.NORTH);
  }

  private void addTextArea() {
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    var scroll = new JScrollPane(textArea);
    scroll.setPreferredSize(new Dimension(600, 200));
    scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(scroll, BorderLayout.CENTER);
  }

  private void addActions() {
    var sendButton = new Button("Envoyer", e -> replyComment());
    var cancelButton = new Button("Annuler", e -> dispose());

    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(sendButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  public String getContent() {
    return textArea.getText().trim();
  }

  private void replyComment() {
    var fileContext = file.get().orElseThrow();

    if (getContent().isBlank()) {
      showError("Le contenu du commentaire ne peut pas être vide.");
      return;
    }

    PendingCommentManager.add(new ReplyComment(fileContext.getDriveId(), getContent(), comment));
    refresh.run();
  }
}
