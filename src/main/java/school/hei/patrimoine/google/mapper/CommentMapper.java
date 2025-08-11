package school.hei.patrimoine.google.mapper;

import java.time.Instant;
import school.hei.patrimoine.google.model.Comment;

public class CommentMapper {
  public static final CommentMapper INSTANCE = new CommentMapper();

  private CommentMapper() {}

  public Comment toDomain(com.google.api.services.drive.model.Comment comment) {
    return Comment.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .resolved(comment.getResolved())
        .author(UserMapper.INSTANCE.toDomain(comment.getAuthor()))
        .createdAt(Instant.parse(comment.getCreatedTime().toString()))
        .build();
  }

  public com.google.api.services.drive.model.Comment toGoogle(Comment comment) {
      return new com.google.api.services.drive.model.Comment().setContent(comment.content()).setResolved(comment.resolved());
  }

    public com.google.api.services.drive.model.Reply toGoogleReply(Comment comment) {
        return new com.google.api.services.drive.model.Reply().setContent(comment.content());
    }
}
