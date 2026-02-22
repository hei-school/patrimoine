package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import lombok.Getter;
import school.hei.patrimoine.google.model.Comment;

@Getter
public class NotSynchronizedComment extends Comment {
    private final boolean isDeleted;

    public NotSynchronizedComment(Comment comment) {
      this(comment, false);
    }

    public NotSynchronizedComment(Comment comment, boolean isDeleted) {
        super(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getAuthor(),
            comment.isResolved(),
            comment.getAnswers()
        );
        this.isDeleted = isDeleted;
    }
}
