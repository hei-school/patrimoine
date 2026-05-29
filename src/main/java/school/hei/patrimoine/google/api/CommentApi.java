package school.hei.patrimoine.google.api;

import static java.util.Comparator.comparing;

import com.google.api.services.drive.model.Reply;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
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
            .setPageSize(pagination.getPageSize())
            .setPageToken(pagination.getCurrentToken())
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
            .sorted(comparing(Comment::getCreatedAt).reversed())
            .toList();

    var nexPagination =
        pagination.toBuilder()
            .currentToken(pagination.getCurrentToken())
            .nextToken(nextPageToken)
            .prevToken(pagination.getPrevToken())
            .build();
    return PaginatedResult.of(results, nexPagination);
  }

  public String add(String fileId, String content) throws GoogleIntegrationException {
    try {
      var newComment = Comment.builder().content(content).build();

      var added =
          driveApi
              .driveService()
              .comments()
              .create(fileId, commentMapper.toGoogle(newComment))
              .setFields("id")
              .execute();

      return added.getId();
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
      throw new GoogleIntegrationException("Failed to reply to comment " + commentId, e);
    }
  }

  public void resolve(String fileId, String commentId) throws GoogleIntegrationException {
    try {
      var resolveReply =
          new Reply().setContent("This comment has been resolved.").setAction("resolve");

      driveApi
          .driveService()
          .replies()
          .create(fileId, commentId, resolveReply)
          .setFields("id")
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to resolve comment " + commentId, e);
    }
  }

  public void delete(String fileId, String commentId) throws GoogleIntegrationException {
    try {
      driveApi.driveService().comments().delete(fileId, commentId).execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to delete comment " + commentId, e);
    }
  }
}
