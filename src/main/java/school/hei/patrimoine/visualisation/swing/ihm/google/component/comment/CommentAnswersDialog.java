package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentCard.replyButton;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentCard.resolveButton;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentAnswersDialog extends Dialog {
  private final String fileId;
  private final Comment parentComment;
  private final CommentListPanel commentListPanel;
  private final Runnable refresh;

  public CommentAnswersDialog(String fileId, Comment parentComment, Runnable refreshParent) {
    super("Réponses au commentaire", 800, 600, false);
    this.fileId = fileId;
    this.refresh =
        () -> {
          dispose();
          refreshParent.run();
        };

    this.parentComment = parentComment;
    this.commentListPanel = new CommentListPanel(this, false, this::dispose);

    setModal(true);
    setLayout(new BorderLayout());

    addCommentList();
    addActions();
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            CommentAnswersDialog.this.update();
          }
        });

    pack();
    setVisible(true);
  }

  private void addCommentList() {
    var scroll =
        new JScrollPane(
            commentListPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
    add(scroll, BorderLayout.CENTER);
    update();
  }

  private void addActions() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(replyButton(fileId, parentComment, refresh));
    buttonPanel.add(resolveButton(fileId, parentComment, refresh));
    buttonPanel.add(new Button("Fermer", e -> dispose()));
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void update() {
    var comments = new ArrayList<>(List.of(parentComment));
    comments.addAll(parentComment.answers());

    commentListPanel.update(fileId, comments);
  }
}
