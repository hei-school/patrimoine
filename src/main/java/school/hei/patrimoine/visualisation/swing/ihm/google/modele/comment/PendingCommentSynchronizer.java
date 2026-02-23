package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.commentApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.getPendings;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.remove;

import java.util.List;
import java.util.Optional;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.*;

public class PendingCommentSynchronizer {
  private static Optional<Comment> sync(AbstractPendingComment pending)
      throws GoogleIntegrationException {
    var fileId = pending.getFile().getDriveId();

    return switch (pending) {
      case AddComment toAdd -> Optional.ofNullable(commentApi().add(fileId, toAdd.getContent()));
      case DeleteComment toDelete -> {
        commentApi().delete(fileId, toDelete.getComment().getId());
        yield Optional.empty();
      }
      case ResolveComment toResolve -> {
        commentApi().resolve(fileId, toResolve.getComment().getId());
        yield Optional.empty();
      }
      case ReplyComment toReply -> {
        commentApi().reply(fileId, toReply.getComment().getId(), toReply.getContent());
        yield Optional.empty();
      }
      case GroupedByComment group -> {
        var subs = group.getSortedPendings();
        var rawComment = group.getRawComment();

        if (rawComment instanceof NotSynchronizedComment) {
          rawComment =
              sync(new AddComment(pending.getFile(), rawComment.getContent())).orElseThrow();
        }

        for (var subPending : mapRawComment(rawComment, subs)) {
          sync(subPending);
          remove(subPending);
        }

        yield Optional.empty();
      }
      default -> throw new RuntimeException("Unknown pending comment");
    };
  }

  public static void sync() throws GoogleIntegrationException {
    for (var pending : getPendings()) {
      sync(pending);
      remove(pending);
    }
  }

  private static List<AbstractPendingComment> mapRawComment(
      Comment rawComment, List<AbstractPendingComment> pendings) {
    return pendings.stream()
        .map(
            pending -> {
              var file = pending.getFile();
              return switch (pending) {
                case ReplyComment toReply ->
                    new ReplyComment(file, toReply.getContent(), rawComment);
                case DeleteComment ignored -> new DeleteComment(file, rawComment);
                case ResolveComment ignored -> new ResolveComment(file, rawComment);
                default ->
                    throw new RuntimeException("Unexpected type of pending of mapRawComment");
              };
            })
        .toList();
  }
}
