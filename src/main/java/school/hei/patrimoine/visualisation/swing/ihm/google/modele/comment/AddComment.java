package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import lombok.Builder;
import school.hei.patrimoine.google.model.User;

import java.time.Instant;

@Builder
public record AddComment(String localId,
                         String fileId,
                         String content,
                         Instant createdAt,
                         User user) implements PendingComment {
}
