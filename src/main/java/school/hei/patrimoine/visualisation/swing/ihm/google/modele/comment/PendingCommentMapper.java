package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment;

import java.util.ArrayList;
import java.util.List;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending.*;

public class PendingCommentMapper {
  public static Comment map(AbstractPendingComment comment) {
    return switch (comment) {
      case AddComment toDelete -> new NotSynchronizedComment(map(toDelete));
      default -> {
        var group = (GroupedByComment) comment;
        var rawComment = group.getRawComment();

        if (group.isDeleted()) {
          yield new NotSynchronizedComment(rawComment, true);
        }

        var answers = getAnswers(group);
        rawComment.addAnswers(answers);
        if (group.isResolved()) {
          rawComment.resolve();
        }

        yield rawComment;
      }
    };
  }

  private static List<Comment> getAnswers(GroupedByComment group) {
    return group.getSortedReplies().stream().map(PendingCommentMapper::map).toList();
  }

  private static Comment map(ReplyComment toReply) {
    return Comment.builder()
        .resolved(false)
        .author(getAuthor())
        .answers(new ArrayList<>())
        .id(toReply.getLocalId())
        .content(toReply.getContent())
        .createdAt(toReply.getCreatedAt())
        .build();
  }

  private static Comment map(AddComment toAdd) {
    return Comment.builder()
        .resolved(false)
        .author(getAuthor())
        .id(toAdd.getLocalId())
        .content(toAdd.getContent())
        .answers(new ArrayList<>())
        .createdAt(toAdd.getCreatedAt())
        .build();
  }

  private static User getAuthor() {
    return AppContext.getDefault().getData("connected-user");
  }
}
