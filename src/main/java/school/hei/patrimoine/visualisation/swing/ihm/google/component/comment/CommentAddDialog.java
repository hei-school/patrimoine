package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import java.time.Instant;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.AddComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.CommentManager;

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
    String fileId = state.get("selectedFileId");
    if (fileId == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant d'ajouter un commentaire.");
      return;
    }

    if (textArea.getText().trim().isBlank()) {
      showError("Erreur", "Le contenu du commentaire ne peut pas être vide.");
      return;
    }

    var localId = "local_" + UUID.randomUUID().toString().substring(0, 8);
    var content =  textArea.getText().trim();
    User user = AppContext.getDefault().getData("connected-user");
    var addComment = AddComment.builder().localId(localId).fileId(fileId).content(content).createdAt(Instant.now()).user(user).build();
    CommentManager.getInstance().add(fileId, addComment);
    refresh.run();
  }
}
