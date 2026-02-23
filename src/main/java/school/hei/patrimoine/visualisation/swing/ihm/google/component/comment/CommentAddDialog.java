package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.SelectedFileSupplier;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.AddComment;

public class CommentAddDialog extends Dialog {
  private final JTextArea textArea;
  private final Runnable onAddFinish;
  private final SelectedFileSupplier file;

  public CommentAddDialog(SelectedFileSupplier file, Runnable refreshUI) {
    super("Ajouter un commentaire", 600, 400, false);
    this.file = file;
    this.textArea = new JTextArea();
    this.onAddFinish = refreshUI;
    ;

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

  public String getContent() {
    return textArea.getText().trim();
  }

  private void addComment() {
    var optionalSelectedFile = file.get();
    if (optionalSelectedFile.isEmpty()) {
      showError("Veuillez sélectionner un fichier avant d'ajouter un commentaire.");
      return;
    }

    if (getContent().isBlank()) {
      showError("Le contenu du commentaire ne peut pas être vide.");
      return;
    }

    var selectedFile = optionalSelectedFile.get();
    PendingCommentManager.add(new AddComment(selectedFile, getContent()));
    dispose();
    onAddFinish.run();
  }
}
