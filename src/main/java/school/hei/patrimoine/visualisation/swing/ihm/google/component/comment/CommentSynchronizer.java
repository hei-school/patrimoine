package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager.LOCAL_ID_PREFIX;

import com.google.api.services.drive.model.Reply;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingDeletion;

public class CommentSynchronizer {
  private final DriveApi driveApi;
  private final ApiCache apiCache;
  private final CommentMapper commentMapper;
  private final LocalCommentManager localCommentManager;

  public CommentSynchronizer(DriveApi driveApi) {
    this.driveApi = driveApi;
    this.apiCache = ApiCache.getInstance();
    this.commentMapper = CommentMapper.getInstance();
    this.localCommentManager = LocalCommentManager.getInstance();
  }

  public void syncComments(String fileId) throws GoogleIntegrationException {
    try {
      syncCommentWithDrive(fileId);
    } catch (Exception e) {
      throw new GoogleIntegrationException(
          "Failed to synchronize comments for fileId=" + fileId, e);
    }
  }

  private void syncCommentWithDrive(String fileId) throws IOException {
    Map<String, String> localToRemoteIdMap = new HashMap<>();

    var pendingComments = new ArrayList<>(localCommentManager.getPendingComments(fileId));
    var pendingDeletions = new ArrayList<>(localCommentManager.getPendingDeletions(fileId));

    createPendingCommentsOnDrive(fileId, pendingComments, localToRemoteIdMap);

    localCommentManager.remapPendingActions(fileId, localToRemoteIdMap);

    createPendingRepliesOnDrive(fileId, localToRemoteIdMap, pendingDeletions);

    resolvePendingCommentsOnDrive(fileId, pendingDeletions);

    deletePendingCommentsOnDrive(fileId);

    apiCache.invalidate("comments", key -> key.startsWith(fileId));
  }

  private void createPendingCommentsOnDrive(
      String fileId, List<PendingComment> commentsToAdd, Map<String, String> localToRemoteIdMap)
      throws IOException {

    for (var pendingComment : commentsToAdd) {
      var newComment = Comment.builder().content(pendingComment.content()).build();

      var createdComment =
          driveApi
              .driveService()
              .comments()
              .create(fileId, commentMapper.toGoogle(newComment))
              .setFields("id")
              .execute();

      var remoteId = createdComment.getId();
      localToRemoteIdMap.put(pendingComment.localId(), remoteId);

      localCommentManager.removePendingComment(fileId, pendingComment.localId());
    }
  }

  private void createPendingRepliesOnDrive(
      String fileId, Map<String, String> localToRemoteIdMap, List<PendingDeletion> pendingDeletions)
      throws IOException {

    var pendingReplies = new ArrayList<>(localCommentManager.getPendingReplies(fileId));

    for (var pendingReply : pendingReplies) {
      var parentCommentId = pendingReply.parentCommentId();

      if (parentCommentId.startsWith(LOCAL_ID_PREFIX)) {
        parentCommentId = localToRemoteIdMap.get(parentCommentId);
        if (parentCommentId == null) {
          continue;
        }
      }

      var finalParentCommentId = parentCommentId;
      boolean isParentDeleted =
          pendingDeletions.stream()
              .anyMatch(deletion -> deletion.commentId().equals(finalParentCommentId));

      if (isParentDeleted) {
        localCommentManager.removePendingReply(fileId, pendingReply.localId());
        continue;
      }

      var reply = new Reply().setContent(pendingReply.content());
      driveApi
          .driveService()
          .replies()
          .create(fileId, parentCommentId, reply)
          .setFields("id")
          .execute();

      localCommentManager.removePendingReply(fileId, pendingReply.localId());
    }
  }

  private void resolvePendingCommentsOnDrive(String fileId, List<PendingDeletion> pendingDeletions)
      throws IOException {

    var pendingResolutions = new ArrayList<>(localCommentManager.getPendingResolutions(fileId));

    for (var pendingResolution : pendingResolutions) {
      var commentId = pendingResolution.commentId();

      boolean isCommentDeleted =
          pendingDeletions.stream().anyMatch(deletion -> deletion.commentId().equals(commentId));

      if (isCommentDeleted) {
        localCommentManager.removePendingResolution(fileId, pendingResolution.commentId());
        continue;
      }

      if (commentId.startsWith(LOCAL_ID_PREFIX)) {
        continue;
      }

      var resolveReply = new Reply().setContent("A résolu ce commentaire.").setAction("resolve");

      driveApi
          .driveService()
          .replies()
          .create(fileId, commentId, resolveReply)
          .setFields("id")
          .execute();

      localCommentManager.removePendingResolution(fileId, pendingResolution.commentId());
    }
  }

  private void deletePendingCommentsOnDrive(String fileId) throws IOException {
    var remappedPendingDeletions = new ArrayList<>(localCommentManager.getPendingDeletions(fileId));

    for (var pendingDeletion : remappedPendingDeletions) {
      var commentId = pendingDeletion.commentId();

      if (!commentId.startsWith(LOCAL_ID_PREFIX)) {
        driveApi.driveService().comments().delete(fileId, commentId).execute();
      }

      localCommentManager.removePendingDeletion(fileId, pendingDeletion.commentId());
    }
  }
}
