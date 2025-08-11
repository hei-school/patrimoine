package school.hei.patrimoine.google.provider;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import school.hei.patrimoine.google.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.mapper.CommentMapper;
import school.hei.patrimoine.google.model.Comment;

public record CommentProvider(DriveApi driveApi) {
  public List<Comment> getByFileId(String fileId) throws GoogleIntegrationException {
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
          .filter(not(com.google.api.services.drive.model.Comment::getResolved))
          .map(CommentMapper.INSTANCE::toDomain)
          .sorted(Comparator.comparing(Comment::createdAt))
          .toList();

    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to get comments for fileId=" + fileId, e);
    }
  }

  public void add(String fileId, String content) throws GoogleIntegrationException {
    try {
      var newComment = Comment.builder().content(content).build();

      driveApi
          .driveService()
          .comments()
          .create(fileId, CommentMapper.INSTANCE.toGoogle(newComment))
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
          .create(fileId, commentId, CommentMapper.INSTANCE.toGoogleReply(reply))
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
          .update(fileId, commentId, CommentMapper.INSTANCE.toGoogle(updatedComment))
          .execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException("Failed to resolve comment " + commentId, e);
    }
  }
}
