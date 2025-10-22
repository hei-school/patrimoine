package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showInfo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class CommentAddDialog extends Dialog {
  private final State state;
  private final Runnable refresh;
  private final JTextArea textArea;

  public CommentAddDialog(State state, Runnable refreshParent) {
    super("Ajouter un commentaire", 500, 300, false);
    this.state = state;
    this.textArea = new JTextArea();
    this.refresh =
        () -> {
          dispose();
          refreshParent.run();
        };

    setLayout(new BorderLayout());

    addTitle();
    addCommentInput();
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

  private void addCommentInput() {
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    var scroll =
        new JScrollPane(
            textArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    scroll.setPreferredSize(new Dimension(600, 200));
    scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
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
      showError("Erreur", "Veuillez sélectionner un fichier avant d'ajouter un commentaire.");
      return;
    }

    if (textArea.getText().trim().isBlank()) {
      showError("Erreur", "Le contenu du commentaire ne peut pas être vide.");
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
              showInfo("Succès", "Le commentaire a été ajouté avec succès.");
              dispose();
              refresh.run();
            })
        .onError(error -> showError("Error", "Erreur lors de l'envoi du commentaire"))
        .build()
        .execute();
  }
}
