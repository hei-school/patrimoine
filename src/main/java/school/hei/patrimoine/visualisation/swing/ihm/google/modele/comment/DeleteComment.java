package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import lombok.Builder;

import java.time.Instant;

@Builder
public record DeleteComment(String fileId, String commentId, Instant deletedAt) implements PendingComment {
}
