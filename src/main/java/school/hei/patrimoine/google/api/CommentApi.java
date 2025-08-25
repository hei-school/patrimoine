package school.hei.patrimoine.google.api;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;

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

  public List<Comment> getByFileId(String fileId) throws GoogleIntegrationException {
    return apiCache
        .wrap(
            COMMENTS_CACHE_KEY,
            fileId,
            () -> {
              try {
                var commentList =
                    driveApi
                        .driveService()
                        .comments()
                        .list(fileId)
                        .setIncludeDeleted(false)
                        .setFields(
                            "comments(id,content,createdTime,resolved,"
                                + "author(displayName,emailAddress,photoLink,permissionId,me),"
                                + "replies(id,content,createdTime,author(displayName,emailAddress,photoLink,permissionId,me)))"
                                + ",nextPageToken")
                        .execute();

                var comments = Optional.ofNullable(commentList.getComments()).orElse(List.of());

                return comments.stream()
                    .filter(not(c -> c.getResolved() != null && c.getResolved()))
                    .map(commentMapper::toDomain)
                    .sorted(Comparator.comparing(Comment::createdAt))
                    .toList();
              } catch (IOException e) {
                throw new GoogleIntegrationException(
                    "Failed to get comments for fileId=" + fileId, e);
              }
            })
        .get();
  }

  public void add(String fileId, String content) throws GoogleIntegrationException {
    try {
      var newComment = Comment.builder().content(content).build();

      driveApi
          .driveService()
          .comments()
          .create(fileId, commentMapper.toGoogle(newComment))
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
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to reply to comment " + commentId, e);
    }
  }

  public void resolve(String fileId, String commentId) throws GoogleIntegrationException {
    try {
      var updatedComment = Comment.builder().resolved(true).build();

      driveApi
          .driveService()
          .comments()
          .update(fileId, commentId, commentMapper.toGoogle(updatedComment))
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to resolve comment " + commentId, e);
    }
  }
}
