package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.google.model.Comment;

@Getter
@Builder(toBuilder = true)
public class ReplyComment extends AbstractPendingComment {
  private final Comment comment;
  private final String content;

  public ReplyComment(String fileId, String content, Comment comment) {
    super(fileId);
    this.content = content;
    this.comment = comment;
  }
}
