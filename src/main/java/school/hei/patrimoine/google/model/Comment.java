package school.hei.patrimoine.google.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

@Builder(toBuilder = true)
public record Comment(
    String id,
    String content,
    Instant createdAt,
    User author,
    boolean resolved,
    List<Comment> answers) {
  private static final String APPROVED_KEYWORD = "approuvé";

  public boolean isApproved() {
    return answers != null
        && answers.stream()
            .anyMatch(
                answer ->
                    answer.content() != null
                        && answer.content().equalsIgnoreCase(APPROVED_KEYWORD));
  }

  public Instant getLastModifiedDate() {
    if (answers == null || answers.isEmpty()) {
      return createdAt;
    }

    return answers.stream()
        .map(Comment::createdAt)
        .filter(Objects::nonNull)
        .max(Instant::compareTo)
        .orElse(createdAt);
  }
}
