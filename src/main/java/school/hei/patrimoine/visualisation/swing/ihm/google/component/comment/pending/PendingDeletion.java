package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import java.time.Instant;

public record PendingDeletion(
    String fileId, String commentId, Instant deletedAt, SyncStatus status) {}
