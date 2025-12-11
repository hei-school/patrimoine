package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import java.time.Instant;
import lombok.Builder;

@Builder
public record PendingDeletion(
    String fileId, String commentId, Instant deletedAt, SyncStatus status) {}
