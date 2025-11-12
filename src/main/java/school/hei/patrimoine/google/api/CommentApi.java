package school.hei.patrimoine.google.api;

import com.google.api.services.drive.model.Reply;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.PaginatedResult;
import school.hei.patrimoine.google.model.Pagination;

public class CommentApi {
  private final DriveApi driveApi;
  private final ApiCache apiCache;
  private final CommentMapper commentMapper;
  public static final String COMMENTS_CACHE_KEY = "comments";

  public CommentApi(DriveApi driveApi) {
    this.driveApi = driveApi;
    this.apiCache = ApiCache.getInstance();
    this.commentMapper = CommentMapper.getInstance();
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

    if (commentList.getComments() == null) {
      return PaginatedResult.of(List.of(), pagination);
    }

    var comments = commentList.getComments();
    var nextPageToken = commentList.getNextPageToken();
    var results =
        comments.stream()
            .map(commentMapper::toDomain)
            .sorted(Comparator.comparing(Comment::createdAt).reversed())
            .toList();
    return PaginatedResult.of(results, new Pagination(pagination.pageSize(), nextPageToken));
  }

  public void add(String fileId, String content) throws GoogleIntegrationException {
    try {
      var newComment = Comment.builder().content(content).build();

      driveApi
          .driveService()
          .comments()
          .create(fileId, commentMapper.toGoogle(newComment))
          .setFields("id")
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to add comment to fileId=" + fileId, e);
    }
  }

  public void reply(String fileId, String commentId, String content)
      throws GoogleIntegrationException {
    try {
      var reply = Comment.builder().content(content).build();

      driveApi
          .driveService()
          .replies()
          .create(fileId, commentId, commentMapper.toGoogleReply(reply))
          .setFields("id")
          .execute();
    } catch (IOException e) {
      System.out.printf(e.getMessage());
      throw new GoogleIntegrationException("Failed to reply to comment " + commentId, e);
    }
  }

  public void resolve(String fileId, Comment comment) throws GoogleIntegrationException {
    try {
      var resolveReply =
          new Reply().setContent("This comment has been resolved.").setAction("resolve");

      driveApi
          .driveService()
          .replies()
          .create(fileId, comment.id(), resolveReply)
          .setFields("id")
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to resolve comment " + comment.id(), e);
    }
  }

  public void delete(String fileId, Comment comment) throws GoogleIntegrationException {
    try {
      driveApi.driveService().comments().delete(fileId, comment.id()).execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to delete comment " + comment.id(), e);
    }
  }
}
