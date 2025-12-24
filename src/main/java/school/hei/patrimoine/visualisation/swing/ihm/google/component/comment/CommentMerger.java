package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingDeletion;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingReply;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingResolution;

public class CommentMerger {
  private final LocalCommentManager localCommentManager;

  public CommentMerger(LocalCommentManager localCommentManager) {
    this.localCommentManager = localCommentManager;
  }

  public List<Comment> mergeLocalComments(String fileId, List<Comment> driveComments) {
    if (!localCommentManager.hasAnyPendingComments()) {
      return driveComments.stream()
          .sorted(Comparator.comparing(Comment::createdAt).reversed())
          .toList();
    }

    var pendingDeletions = localCommentManager.getPendingDeletions(fileId);
    var pendingComments = localCommentManager.getPendingComments(fileId);
    var pendingResolutions = localCommentManager.getPendingResolutions(fileId);
    var pendingReplies = localCommentManager.getPendingReplies(fileId);

    List<Comment> allComments = new ArrayList<>(driveComments);
    Map<String, String> idMapping = localCommentManager.getIdMappingsForFile(fileId);
    User currentUser = AppContext.getDefault().getData("connected-user");

    removePendingDeletions(allComments, pendingDeletions, idMapping);

    addPendingComments(allComments, pendingComments, pendingDeletions, idMapping, currentUser);

    applyPendingResolutions(allComments, pendingResolutions, pendingDeletions, idMapping);

    addPendingReplies(allComments, pendingReplies, pendingDeletions, idMapping, currentUser);

    return allComments.stream()
        .sorted(Comparator.comparing(Comment::createdAt).reversed())
        .toList();
  }

  private void removePendingDeletions(
      List<Comment> allComments,
      List<PendingDeletion> pendingDeletions,
      Map<String, String> idMapping) {

    allComments.removeIf(
        comment -> {
          String commentId = comment.id();
          return pendingDeletions.stream()
              .anyMatch(
                  deletion -> {
                    String deletionId = deletion.commentId();
                    String normalizedDeletionId = idMapping.getOrDefault(deletionId, deletionId);
                    return normalizedDeletionId.equals(commentId);
                  });
        });
  }

  private void addPendingComments(
      List<Comment> allComments,
      List<PendingComment> pendingComments,
      List<PendingDeletion> pendingDeletions,
      Map<String, String> idMapping,
      User currentUser) {

    for (var pending : pendingComments) {
      if (isCommentDeleted(pending.localId(), pendingDeletions, idMapping)) {
        continue;
      }

      String commentId = idMapping.getOrDefault(pending.localId(), pending.localId());
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
      List<PendingResolution> pendingResolutions,
      List<PendingDeletion> pendingDeletions,
      Map<String, String> idMapping) {

    for (var resolution : pendingResolutions) {
      String commentIdToResolve =
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
      List<PendingReply> pendingReplies,
      List<PendingDeletion> pendingDeletions,
      Map<String, String> idMapping,
      User currentUser) {

    for (var pendingReply : pendingReplies) {
      String parentCommentId =
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

    String normalizedCommentId = idMapping.getOrDefault(commentId, commentId);

    return pendingDeletions.stream()
        .anyMatch(
            deletion -> {
              String deletionId = deletion.commentId();
              String normalizedDeletionId = idMapping.getOrDefault(deletionId, deletionId);
              return normalizedDeletionId.equals(normalizedCommentId);
            });
  }
}
