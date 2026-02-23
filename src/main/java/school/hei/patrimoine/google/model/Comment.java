package school.hei.patrimoine.google.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Getter
@EqualsAndHashCode()
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {
  private final String id;
  private final String content;
  private final Instant createdAt;
  private final User author;

  private boolean resolved;
  private List<Comment> answers;

  private static final String APPROVED_KEYWORD = "approuvé";

  public void resolve() {
    this.resolved = true;
  }

  public void addAnswer(Comment comment) {
    if (this.answers == null) {
      this.answers = new ArrayList<>();
    }
    this.answers.add(comment);
  }

  public void addAnswers(List<Comment> comments) {
    comments.forEach(this::addAnswer);
  }

  public boolean isApproved() {
    return answers != null
        && answers.stream()
            .anyMatch(
                answer ->
                    answer.getContent() != null
                        && answer.getContent().equalsIgnoreCase(APPROVED_KEYWORD));
  }

  public Instant getLastModifiedDate() {
    if (answers == null || answers.isEmpty()) {
      return createdAt;
    }

    return answers.stream()
        .map(Comment::getCreatedAt)
        .filter(Objects::nonNull)
        .max(Instant::compareTo)
        .orElse(createdAt);
  }
}
