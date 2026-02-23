package school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.pending;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class GroupedByComment extends AbstractPendingComment {
  private final Map<String, AbstractPendingComment> pendings;

  public GroupedByComment(PatriLangFileContext file) {
    super(file);
    this.pendings = new ConcurrentHashMap<>();
  }

  public boolean isEmpty() {
    return this.pendings.isEmpty();
  }

  public void remove(AbstractPendingComment comment) {
    this.pendings.remove(comment.getLocalId());
  }

  public void add(AbstractPendingComment comment) {
    if (isDeleted()) {
      throw new RuntimeException("Cannot do anything on a deleted comment");
    }

    var id = comment.getLocalId();
    if (isResolved()) {
      if (!(comment instanceof DeleteComment)) {
        throw new RuntimeException("Cannot do anything on a resolved comment except delete");
      }
      this.pendings.put(id, comment);
      return;
    }

    if (!(comment instanceof ReplyComment)) {
      throw new RuntimeException("Wrong type of comment");
    }

    this.pendings.put(id, comment);
  }

  public boolean isResolved() {
    return getSortedPendings().stream().anyMatch(pending -> pending instanceof ResolveComment);
  }

  public boolean isDeleted() {
    return getSortedPendings().stream().anyMatch(pending -> pending instanceof DeleteComment);
  }

  public List<AbstractPendingComment> getSortedPendings() {
    return new ArrayList<>(pendings.values())
        .stream().sorted(comparing(AbstractPendingComment::getCreatedAt)).toList();
  }

  public List<ReplyComment> getSortedReplies() {
    return getSortedPendings().stream()
        .filter(pending -> pending instanceof ReplyComment)
        .map(pending -> (ReplyComment) pending)
        .toList();
  }

  public Comment getRawComment() {
    return switch (getSortedPendings().getFirst()) {
      case DeleteComment toDelete -> toDelete.getComment();
      case ReplyComment toReply -> toReply.getComment();
      case ResolveComment toResolve -> toResolve.getComment();
      default -> throw new RuntimeException("Invalid type of sub pending");
    };
  }
}
