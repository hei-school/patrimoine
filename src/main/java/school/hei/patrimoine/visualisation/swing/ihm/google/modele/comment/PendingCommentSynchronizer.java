package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.commentApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.getPendings;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.remove;

import java.util.Optional;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.*;

public class PendingCommentSynchronizer {
  private static void sync(GroupedByComment group) throws GoogleIntegrationException {
    var rawComment = group.getRawComment();

    var subs = group.getSortedPendings();
    for (var subPending : subs) {
      var optionalNewRawComment = sync(mapRawComment(rawComment, subPending));
      if (optionalNewRawComment.isPresent()) {
        rawComment = optionalNewRawComment.get();
      }

      remove(subPending);
    }
  }

  private static Optional<Comment> sync(AbstractPendingComment pending)
      throws GoogleIntegrationException {
    var fileId = pending.getFile().getDriveId();

    return switch (pending) {
      case AddComment toAdd -> {
        var newId = commentApi().add(fileId, toAdd.getContent());
        yield Optional.of(PendingCommentMapper.map(toAdd).toBuilder().id(newId).build());
      }
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
      default -> throw new RuntimeException("Unknown pending comment");
    };
  }

  public static void sync() throws GoogleIntegrationException {
    for (var pending : getPendings()) {
      sync(pending);
    }
    PendingCommentManager.clear();
  }

  private static AbstractPendingComment mapRawComment(
      Comment rawComment, AbstractPendingComment pending) {
    var file = pending.getFile();
    return switch (pending) {
      case AddComment toAdd -> new AddComment(file, toAdd.getContent());
      case ReplyComment toReply -> new ReplyComment(file, toReply.getContent(), rawComment);
      case DeleteComment ignored -> new DeleteComment(file, rawComment);
      case ResolveComment ignored -> new ResolveComment(file, rawComment);
      default -> throw new RuntimeException("Unexpected type of pending of mapRawComment");
    };
  }
}
