package school.hei.patrimoine.google.api;

import com.google.api.services.drive.model.Reply;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingComment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingDeletion;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingReply;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.pending.PendingResolution;

public class CommentApi {
  private final DriveApi driveApi;
  private final ApiCache apiCache;
  private final CommentMapper commentMapper;
  private final LocalCommentManager localCommentManager;
  public static final String COMMENTS_CACHE_KEY = "comments";

  public CommentApi(DriveApi driveApi) {
    this.driveApi = driveApi;
    this.apiCache = ApiCache.getInstance();
    this.commentMapper = CommentMapper.getInstance();
    this.localCommentManager = LocalCommentManager.getInstance();
  }

  public PaginatedResult<List<Comment>> getByFileId(
      String fileId, Pagination pagination, Instant startDate) throws GoogleIntegrationException {

    return apiCache
        .wrap(
            COMMENTS_CACHE_KEY,
            pagination.createCacheKey(fileId + "_" + startDate),
            () -> {
              try {
                return getByFileIdWithoutCache(fileId, pagination, startDate);
              } catch (IOException e) {
                throw new GoogleIntegrationException(
                    "Failed to get comments for fileId=" + fileId, e);
              }
            })
        .get();
  }

  private PaginatedResult<List<Comment>> getByFileIdWithoutCache(
      String fileId, Pagination pagination, Instant startDate) throws IOException {

    var startDateStr = DateTimeFormatter.ISO_INSTANT.format(startDate);

    var commentList =
        driveApi
            .driveService()
            .comments()
            .list(fileId)
            .setIncludeDeleted(false)
            .setStartModifiedTime(startDateStr)
            .setPageSize(pagination.pageSize())
            .setPageToken(pagination.pageToken())
            .setFields(
                "comments(id,content,createdTime,resolved,"
                    + "author(displayName,emailAddress,photoLink,permissionId,me),"
                    + "replies(id,content,createdTime,author(displayName,emailAddress,photoLink,permissionId,me)))"
                    + ",nextPageToken")
            .execute();

    List<Comment> allComments = new ArrayList<>();

    if (commentList.getComments() != null) {
      var driveComments =
          commentList.getComments().stream()
              .map(commentMapper::toDomain)
              .sorted(Comparator.comparing(Comment::createdAt).reversed())
              .toList();
      allComments.addAll(driveComments);
    }
    var mergedComments = mergeLocalComments(fileId, allComments);

    var nextPageToken = commentList.getNextPageToken();
    return PaginatedResult.of(mergedComments, new Pagination(pagination.pageSize(), nextPageToken));
  }

  private List<Comment> mergeLocalComments(String fileId, List<Comment> driveComments) {
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

  public void add(String fileId, String content) throws GoogleIntegrationException {
    try {
      User currentUser = AppContext.getDefault().getData("connected-user");
      localCommentManager.addPendingComment(fileId, content, currentUser);
    } catch (Exception e) {
      throw new GoogleIntegrationException("Failed to add local comment to fileId=" + fileId, e);
    }
  }

  public void reply(String fileId, String commentId, String content)
      throws GoogleIntegrationException {
    try {
      User currentUser = AppContext.getDefault().getData("connected-user");
      localCommentManager.addPendingReply(fileId, commentId, content, currentUser);
    } catch (Exception e) {
      throw new GoogleIntegrationException("Failed to local reply to comment " + commentId, e);
    }
  }

  public void resolve(String fileId, Comment comment) throws GoogleIntegrationException {
    try {
      localCommentManager.addPendingResolution(fileId, comment.id());
    } catch (Exception e) {
      throw new GoogleIntegrationException("Failed to resolve comment locally " + comment.id(), e);
    }
  }

  public void delete(String fileId, Comment comment) throws GoogleIntegrationException {
    try {
      if (comment.id().startsWith("local_")) {
        localCommentManager.removePendingComment(fileId, comment.id());
      } else {
        localCommentManager.addPendingDeletion(fileId, comment.id());
      }
      localCommentManager.cleanUpCommentActions(fileId, comment.id());
    } catch (Exception e) {
      throw new GoogleIntegrationException("Failed to delete comment locally " + comment.id(), e);
    }
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

    apiCache.invalidate(COMMENTS_CACHE_KEY, key -> key.startsWith(fileId));
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

      String remoteId = createdComment.getId();
      localToRemoteIdMap.put(pendingComment.localId(), remoteId);

      localCommentManager.removePendingComment(fileId, pendingComment.localId());
    }
  }

  private void createPendingRepliesOnDrive(
      String fileId, Map<String, String> localToRemoteIdMap, List<PendingDeletion> pendingDeletions)
      throws IOException {

    var pendingReplies = new ArrayList<>(localCommentManager.getPendingReplies(fileId));

    for (var pendingReply : pendingReplies) {
      String parentCommentId = pendingReply.parentCommentId();

      if (parentCommentId.startsWith("local_")) {
        parentCommentId = localToRemoteIdMap.get(parentCommentId);
        if (parentCommentId == null) {
          continue;
        }
      }

      String finalParentCommentId = parentCommentId;
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
      String commentId = pendingResolution.commentId();

      boolean isCommentDeleted =
          pendingDeletions.stream().anyMatch(deletion -> deletion.commentId().equals(commentId));

      if (isCommentDeleted) {
        localCommentManager.removePendingResolution(fileId, pendingResolution.commentId());
        continue;
      }

      if (commentId.startsWith("local_")) {
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
      String commentId = pendingDeletion.commentId();

      if (!commentId.startsWith("local_")) {
        driveApi.driveService().comments().delete(fileId, commentId).execute();
      }

      localCommentManager.removePendingDeletion(fileId, pendingDeletion.commentId());
    }
  }
}
