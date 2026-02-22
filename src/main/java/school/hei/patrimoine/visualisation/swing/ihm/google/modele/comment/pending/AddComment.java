package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AddComment extends AbstractPendingComment {
  private final String content;

  public AddComment(String fileId, String content) {
    super(fileId);
    this.content = content;
  }
}
