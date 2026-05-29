package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Getter;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

@Getter
public class ResolveComment extends AbstractPendingComment {
  private final Comment comment;

  public ResolveComment(PatriLangFileContext file, Comment comment) {
    super(file);
    this.comment = comment;
  }
}
