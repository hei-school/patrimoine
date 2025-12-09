package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import java.time.Instant;
import lombok.Builder;

@Builder
public record PendingResolution(
    String fileId, String commentId, Instant resolvedAt, SyncStatus status) {}
