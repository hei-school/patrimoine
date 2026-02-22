package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.google.model.Comment;

@Getter
@Builder(toBuilder = true)
public class ResolveComment extends AbstractPendingComment {
  private final Comment comment;

  public ResolveComment(String fileId, Comment comment) {
    super(fileId);
    this.comment = comment;
  }
}
