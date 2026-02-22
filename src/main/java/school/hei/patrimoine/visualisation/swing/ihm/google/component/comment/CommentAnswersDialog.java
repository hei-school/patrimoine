package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentCard.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.SelectedFileSupplier;

public class CommentAnswersDialog extends Dialog {
  private final Comment comment;
  private final Runnable refresh;
  private final SelectedFileSupplier file;
  private final CommentListPanel answersPanel;

  public CommentAnswersDialog(SelectedFileSupplier file, Comment comment, Component parent, Runnable refreshUI) {
    super("Réponses au commentaire", 800, 500, false);
    this.file = file;
    this.refresh =
        () -> {
          dispose();
          refreshUI.run();
        };

    this.comment = comment;
    this.answersPanel = new CommentListPanel(parent, false, refresh);

    setModal(true);
    setLayout(new BorderLayout());

    addCommentList();
    addActions();

    pack();
    setVisible(true);
  }

  private void addCommentList() {
    add(answersPanel.toScrollPane(), BorderLayout.CENTER);
    update();
  }

  private void addActions() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    if (!comment.isResolved()) {
      buttonPanel.add(replyButton(file, comment, refresh));
      buttonPanel.add(resolveButton(file, comment, refresh));
    }
    buttonPanel.add(new Button("Fermer", e -> dispose()));

    if(!comment.getAuthor().me()){
      add(buttonPanel, BorderLayout.SOUTH);
      return;
    }

    buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    var removeBtn = removeButton(file, comment, refresh);
    removeBtn.setMargin(new Insets(0, 50, 0, 50));
    buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    buttonPanel.add(removeBtn);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void update() {
    var comments = new ArrayList<>(List.of(comment));
    comments.addAll(comment.getAnswers());
    answersPanel.update(file, comments);
  }
}
