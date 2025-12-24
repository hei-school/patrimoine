package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class LocalCommentActions {
  private final LocalCommentManager localCommentManager;

  public LocalCommentActions(LocalCommentManager localCommentManager) {
    this.localCommentManager = localCommentManager;
  }

  public void add(String fileId, String content) {
    localCommentManager.addPendingComment(fileId, content, getCurrentUser());
  }

  public void reply(String fileId, String commentId, String content) {
    localCommentManager.addPendingReply(fileId, commentId, content, getCurrentUser());
  }

  public void resolve(String fileId, Comment comment) {
    localCommentManager.addPendingResolution(fileId, comment.id());
  }

  public void delete(String fileId, Comment comment) {
    if (isLocalComment(comment)) {
      localCommentManager.removePendingComment(fileId, comment.id());
    } else {
      localCommentManager.addPendingDeletion(fileId, comment.id());
    }
    localCommentManager.cleanUpCommentActions(fileId, comment.id());
  }

  private User getCurrentUser() {
    return AppContext.getDefault().getData("connected-user");
  }

  public boolean canDelete(Comment comment) {
    return isLocalComment(comment) || (comment.author() != null && comment.author().me());
  }

  private boolean isLocalComment(Comment comment) {
    return comment.id().startsWith("local_");
  }
}
