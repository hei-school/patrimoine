package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;

public class CommentReplyDialog extends Dialog {
  private final LocalCommentActions localCommentActions;
  private final String fileId;
  private final Comment parentComment;
  private final JTextArea textArea;
  private final Runnable refresh;

  public CommentReplyDialog(
      LocalCommentActions localCommentActions,
      String fileId,
      Comment parentComment,
      Runnable refresh) {
    super("Répondre au commentaire", 500, 300, false);

    this.localCommentActions = localCommentActions;
    this.fileId = fileId;
    this.refresh = refresh;
    this.textArea = new JTextArea();
    this.parentComment = parentComment;

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

  private void replyComment() {
    if (textArea.getText().trim().isBlank()) {
      showError("Erreur", "Le contenu du commentaire ne peut pas être vide.");
      return;
    }

    AsyncTask.<Void>builder()
        .task(
            () -> {
              localCommentActions.reply(fileId, parentComment.id(), textArea.getText().trim());
              return null;
            })
        .withDialogLoading(false)
        .onSuccess(
            result -> {
              dispose();
              refresh.run();
            })
        .onError(error -> showError("Error", "Erreur lors de l'envoi du commentaire"))
        .build()
        .execute();
  }
}
