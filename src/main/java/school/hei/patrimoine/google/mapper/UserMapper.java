package school.hei.patrimoine.google.mapper;

import school.hei.patrimoine.google.model.User;

public class UserMapper {
  private static final UserMapper INSTANCE = new UserMapper();

  private UserMapper() {}

  public User toDomain(com.google.api.services.drive.model.User user) {
    return User.builder()
        .id(user.getPermissionId())
        .displayName(user.getDisplayName())
        .email(user.getEmailAddress())
        .avatarUrl(user.getPhotoLink())
        .me(user.getMe() != null && user.getMe())
        .build();
  }

  public static UserMapper getInstance() {
    return INSTANCE;
  }
}
