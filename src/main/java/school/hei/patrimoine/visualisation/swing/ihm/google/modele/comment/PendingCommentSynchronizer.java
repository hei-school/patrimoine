package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.*;

import java.util.List;
import java.util.Optional;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.commentApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.getPendings;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager.remove;

public class PendingCommentSynchronizer {
    private static Optional<Comment> sync(AbstractPendingComment pending) throws GoogleIntegrationException {
      return switch (pending){
        case AddComment toAdd -> Optional.ofNullable(commentApi().add(toAdd.getFileId(), toAdd.getContent()));
        case DeleteComment toDelete -> {
          commentApi().delete(toDelete.getFileId(), toDelete.getComment().getId());
          yield Optional.empty();
        }
        case ResolveComment toResolve -> {
          commentApi().resolve(toResolve.getFileId(), toResolve.getComment().getId());
          yield Optional.empty();
        }
        case ReplyComment toReply -> {
          commentApi().reply(toReply.getFileId(), toReply.getComment().getId(), toReply.getContent());
          yield Optional.empty();
        }
        case GroupedByComment group -> {
          var subs = group.getSortedPendings();
          var first = group.getSortedPendings().getFirst();
          var rawComment = group.getRawComment();

          if(rawComment instanceof NotSynchronizedComment){
            rawComment = sync(new AddComment(first.getLocalId(), rawComment.getContent())).orElseThrow();
          }

          for(var subPending: mapRawComment(rawComment, subs)){
            sync(subPending);
            remove(subPending);
          }

          yield Optional.empty();
        }
        default -> throw new RuntimeException("Unknown pending comment");
      };
    }

    public static void sync() throws GoogleIntegrationException {
      for(var pending: getPendings()){
        sync(pending);
        remove(pending);
      }
    }

    private static List<AbstractPendingComment> mapRawComment(Comment rawComment, List<AbstractPendingComment> pendings){
        return pendings
            .stream()
            .map(pending ->
                switch(pending){
                    case ReplyComment toReply -> toReply.toBuilder().comment(rawComment).build();
                    case DeleteComment toDelete -> toDelete.toBuilder().comment(rawComment).build();
                    case ResolveComment toResolve -> toResolve.toBuilder().comment(rawComment).build();
                    default -> throw new RuntimeException("Unexpected type of pending of mapRawComment");
                })
            .toList();
    }
}
