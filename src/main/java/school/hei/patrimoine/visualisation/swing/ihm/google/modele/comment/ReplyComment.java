package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import lombok.Builder;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;

import java.time.Instant;

@Builder
public record ReplyComment(String localId,
                          String fileId,
                          Comment parent,
                          String content,
                          Instant createdAt,
                          User user) implements PendingComment {
}
