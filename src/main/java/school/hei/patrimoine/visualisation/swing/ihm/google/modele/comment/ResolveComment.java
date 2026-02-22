package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ResolveComment(String fileId, String commentId, Instant resolvedAt) implements PendingComment {
}
