package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Getter;

import java.time.Instant;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Getter
public abstract class AbstractPendingComment {
  protected final String fileId;
  protected final String localId;
  protected final Instant createdAt;

  public AbstractPendingComment(String fileId) {
    this.fileId = fileId;
    this.createdAt = now();
    this.localId = randomUUID().toString();
  }
}
