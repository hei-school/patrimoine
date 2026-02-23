package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

@Getter
public class AddComment extends AbstractPendingComment {
  private final String content;

  public AddComment(PatriLangFileContext file, String content) {
    super(file);
    this.content = content;
  }
}
