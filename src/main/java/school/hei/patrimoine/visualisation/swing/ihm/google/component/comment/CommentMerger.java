package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingDeletion;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.CommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingComment;

public class CommentMerger {
  private final CommentManager commentManager;

  public CommentMerger(CommentManager commentManager) {
    this.commentManager = commentManager;
  }

  public List<Comment> mergeLocalComments(String fileId, List<Comment> driveComments) {
    var pendingComments = commentManager.getPendingsByFileId(fileId);
      if (pendingComments.isEmpty()) {
      return driveComments.stream()
          .sorted(Comparator.comparing(Comment::createdAt).reversed())
          .toList();
    }

    var allComments = new ArrayList<>(driveComments);
    Map<String, String> idMapping = commentManager.getIdMappingsForFile(fileId);
    User currentUser = AppContext.getDefault().getData("connected-user");
// really need to separate these 4 methods?
    removePendingDeletions(allComments, pendingComments, idMapping);

    addPendingComments(allComments, pendingComments, idMapping, currentUser);

    applyPendingResolutions(allComments, pendingComments, idMapping);

    addPendingReplies(allComments, pendingComments, idMapping, currentUser);

    return allComments.stream()
        .sorted(Comparator.comparing(Comment::createdAt).reversed())
        .toList();
  }

  private void removePendingDeletions(
      List<Comment> allComments,
      List<PendingComment> deleteComments,
      Map<String, String> idMapping) {

    allComments.removeIf(
        comment -> {
          var commentId = comment.id();
          return deleteComments.stream()
              .anyMatch(
                  deletion -> {
                    var deletionId = deletion.commentId();
                    var normalizedDeletionId = idMapping.getOrDefault(deletionId, deletionId);
                    return normalizedDeletionId.equals(commentId);
                  });
        });
  }

  private void addPendingComments(
      List<Comment> allComments,
      List<PendingComment> pendingComments,
      Map<String, String> idMapping,
      User currentUser) {

    for (var pending : pendingComments) {
      if (isCommentDeleted(pending.localId(), pendingDeletions, idMapping)) {
        continue;
      }

      var commentId = idMapping.getOrDefault(pending.localId(), pending.localId());
      var localComment =
          Comment.builder()
              .id(commentId)
              .content(pending.content())
              .createdAt(pending.createdAt())
              .author(pending.author() != null ? pending.author() : currentUser)
              .resolved(false)
              .answers(new ArrayList<>())
              .build();

      allComments.add(localComment);
    }
  }

  private void applyPendingResolutions(
      List<Comment> allComments,
      List<PendingComment> pendingResolutions,
      Map<String, String> idMapping) {

    for (var resolution : pendingResolutions) {
      var commentIdToResolve =
          idMapping.getOrDefault(resolution.commentId(), resolution.commentId());

      if (isCommentDeleted(commentIdToResolve, pendingDeletions, idMapping)) {
        continue;
      }

      allComments.stream()
          .filter(comment -> comment.id().equals(commentIdToResolve))
          .findFirst()
          .ifPresent(
              comment -> {
                if (!comment.resolved()) {
                  var resolvedComment = comment.toBuilder().resolved(true).build();
                  allComments.set(allComments.indexOf(comment), resolvedComment);
                }
              });
    }
  }

  private void addPendingReplies(
      List<Comment> allComments,
      List<PendingComment> pendingReplies,
      Map<String, String> idMapping,
      User currentUser) {

    for (var pendingReply : pendingReplies) {
      var parentCommentId =
          idMapping.getOrDefault(pendingReply.parentCommentId(), pendingReply.parentCommentId());

      if (isCommentDeleted(parentCommentId, pendingDeletions, idMapping)) {
        continue;
      }

      allComments.stream()
          .filter(comment -> comment.id().equals(parentCommentId))
          .findFirst()
          .ifPresent(
              parentComment -> {
                boolean replyExists =
                    parentComment.answers().stream()
                        .anyMatch(answer -> answer.id().equals(pendingReply.localId()));

                if (!replyExists) {
                  var localReply =
                      Comment.builder()
                          .id(pendingReply.localId())
                          .content(pendingReply.content())
                          .createdAt(pendingReply.createdAt())
                          .author(
                              pendingReply.author() != null ? pendingReply.author() : currentUser)
                          .resolved(false)
                          .answers(new ArrayList<>())
                          .build();

                  var updatedAnswers = new ArrayList<>(parentComment.answers());
                  updatedAnswers.add(localReply);

                  var updatedComment = parentComment.toBuilder().answers(updatedAnswers).build();

                  allComments.set(allComments.indexOf(parentComment), updatedComment);
                }
              });
    }
  }

  private boolean isCommentDeleted(
      String commentId, List<PendingDeletion> pendingDeletions, Map<String, String> idMapping) {

    var normalizedCommentId = idMapping.getOrDefault(commentId, commentId);

    return pendingDeletions.stream()
        .anyMatch(
            deletion -> {
              var deletionId = deletion.commentId();
              var normalizedDeletionId = idMapping.getOrDefault(deletionId, deletionId);
              return normalizedDeletionId.equals(normalizedCommentId);
            });
  }
}
