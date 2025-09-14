package school.hei.patrimoine.google.mapper;

import java.time.Instant;
import java.util.List;
import school.hei.patrimoine.google.model.Comment;

public class CommentMapper {
  private static final CommentMapper INSTANCE = new CommentMapper();
  private final UserMapper userMapper;

  private CommentMapper() {
    this.userMapper = UserMapper.getInstance();
  }

  public static CommentMapper getInstance() {
    return INSTANCE;
  }

  public Comment toDomain(com.google.api.services.drive.model.Comment comment) {
    List<Comment> answers =
        comment.getReplies() == null
            ? List.of()
            : comment.getReplies().stream().map(this::replyToDomain).toList();

    return Comment.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .resolved(comment.getResolved() != null && comment.getResolved())
        .author(userMapper.toDomain(comment.getAuthor()))
        .createdAt(Instant.parse(comment.getCreatedTime().toString()))
        .answers(answers)
        .build();
  }

  public com.google.api.services.drive.model.Comment toGoogle(Comment comment) {
    return new com.google.api.services.drive.model.Comment()
        .setId(comment.id())
        .setContent(comment.content())
        .setResolved(comment.resolved());
  }

  public com.google.api.services.drive.model.Reply toGoogleReply(Comment comment) {
    return new com.google.api.services.drive.model.Reply().setContent(comment.content());
  }

  public Comment replyToDomain(com.google.api.services.drive.model.Reply comment) {
    return Comment.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .author(userMapper.toDomain(comment.getAuthor()))
        .createdAt(Instant.parse(comment.getCreatedTime().toString()))
        .answers(List.of())
        .build();
  }
}
