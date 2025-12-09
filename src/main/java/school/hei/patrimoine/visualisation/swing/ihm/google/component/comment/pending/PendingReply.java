package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending;

import java.time.Instant;
import lombok.Builder;
import school.hei.patrimoine.google.model.User;

@Builder
public record PendingReply(
    String localId,
    String fileId,
    String parentCommentId,
    String content,
    Instant createdAt,
    User author,
    SyncStatus status) {}
