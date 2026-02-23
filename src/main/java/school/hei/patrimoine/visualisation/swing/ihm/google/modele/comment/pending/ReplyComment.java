package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Getter;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

@Getter
public class ReplyComment extends AbstractPendingComment {
  private final Comment comment;
  private final String content;

  public ReplyComment(PatriLangFileContext file, String content, Comment comment) {
    super(file);
    this.content = content;
    this.comment = comment;
  }
}
