package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import java.time.Instant;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

@Getter
public abstract class AbstractPendingComment {
  protected final String localId;
  protected final Instant createdAt;
  protected final PatriLangFileContext file;

  public AbstractPendingComment(PatriLangFileContext file) {
    this.file = file;
    this.createdAt = now();
    this.localId = randomUUID().toString();
  }
}
