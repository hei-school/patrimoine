package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog;

public class CommentAddDialog extends Dialog {
  private final State state;
  private final Runnable refresh;
  private final JTextArea textArea;

  public CommentAddDialog(State state, Runnable refreshParent) {
    super("Ajouter une réponse", 500, 300, true);
    this.state = state;
    this.textArea = new JTextArea();
    this.refresh =
        () -> {
          dispose();
          refreshParent.run();
        };

    setLayout(new BorderLayout());

    addCommentInput();
    addActions();

    pack();
    setVisible(true);
  }

  private void addCommentInput() {
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    var scroll =
        new JScrollPane(
            textArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    scroll.setPreferredSize(new Dimension(600, 200));
    add(scroll, BorderLayout.CENTER);
  }

  private void addActions() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(new Button("Envoyer", e -> addComment()));
    buttonPanel.add(new Button("Annuler", e -> dispose()));
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void addComment() {
    if (state.get("selectedFileId") == null) {
      MessageDialog.error(
          "Erreur", "Veuillez sélectionner un fichier avant d'ajouter un commentaire.");
      return;
    }

    if (textArea.getText().trim().isBlank()) {
      MessageDialog.error("Erreur", "Le contenu du commentaire ne peut pas être vide.");
      return;
    }

    CommentApi commentApi = AppContext.getDefault().getData("comment-api");
    AsyncTask.<Void>builder()
        .task(
            () -> {
              commentApi.add(state.get("selectedFileId"), textArea.getText().trim());
              return null;
            })
        .loadingMessage("Envoi en cours...")
        .onSuccess(
            result -> {
              MessageDialog.info("Succès", "Le commentaire a été ajouté avec succès.");
              dispose();
              refresh.run();
            })
        .onError(error -> MessageDialog.error("Error", "Erreur lors de l'envoi du commentaire"))
        .build()
        .execute();
  }
}
