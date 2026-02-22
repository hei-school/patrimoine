package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentMapper;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.NotSynchronizedComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

import java.time.Instant;
import java.util.*;

import static java.util.Comparator.comparing;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.commentApi;

public class CommentsProvider {
  public static PaginatedResult<List<Comment>> getByFile(PatriLangFileContext file, Pagination pagination, Instant startDate) throws GoogleIntegrationException {
    Map<String, Comment> comments = new LinkedHashMap<>();
    var apiResult = commentApi().getByFileId(file.getDriveId(), pagination, startDate);

    for(var comment: apiResult.data()){
      comments.put(comment.getId(), comment);
    }

    for(var pendingComment : PendingCommentManager.getByFile(file)){
      var comment = PendingCommentMapper.map(pendingComment);
      if(comment instanceof NotSynchronizedComment toSync){
        if(toSync.isDeleted()){
          comments.remove(toSync.getId());
          continue;
        }
      }
      comments.put(comment.getId(), comment);
    }

    return apiResult.toBuilder().data(sorted(comments.values())).build();
  }

  private static List<Comment> sorted(Collection<Comment> comments){
    return comments.stream().sorted(comparing(Comment::getLastModifiedDate)).toList();
  }
}
