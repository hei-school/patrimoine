package school.hei.patrimoine.google.model;

import java.time.Instant;
import java.util.List;
import lombok.Builder;

@Builder
public record Comment(
    String id,
    String content,
    Instant createdAt,
    User author,
    boolean resolved,
    List<Comment> replies) {}
