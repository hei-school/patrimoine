package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import java.time.Instant;

public record PendingResolution(
    String fileId, String commentId, Instant resolvedAt, SyncStatus status) {}
