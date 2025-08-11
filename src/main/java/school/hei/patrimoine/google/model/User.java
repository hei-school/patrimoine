package school.hei.patrimoine.google.model;

import lombok.Builder;

@Builder
public record User(String id, String displayName, String email, String avatarUrl) {}
