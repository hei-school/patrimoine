package school.hei.patrimoine.google.api;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;
import static com.google.api.services.drive.DriveScopes.DRIVE;
import static java.util.Objects.requireNonNull;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import java.io.*;
import java.util.List;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.google.mapper.UserMapper;
import school.hei.patrimoine.google.model.User;

@Slf4j
public class GoogleApi {
  private final UserMapper userMapper;
  private final List<String> scopes;

  public GoogleApi() {
    this.scopes = List.of(DRIVE);
    this.userMapper = UserMapper.getInstance();
  }

  private Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
    var in = new FileInputStream(GoogleApiUtilities.getCredentialFilePath());
    var clientSecrets =
        GoogleClientSecrets.load(GoogleApiUtilities.getJsonFactory(), new InputStreamReader(in));

    // Build flow and trigger user authorization
    var flow =
        new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, GoogleApiUtilities.getJsonFactory(), clientSecrets, scopes)
            .setDataStoreFactory(
                new FileDataStoreFactory(
                    new java.io.File(GoogleApiUtilities.getTokenDirectoryPath())))
            .setAccessType("offline")
            .build();
    var receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  @SneakyThrows
  public AuthDetails requestAuthentication() {
    var httpTransport = newTrustedTransport();
    var currentCredentials = getCredentials(httpTransport);
    var credentials =
        AuthDetails.builder().httpTransport(httpTransport).credential(currentCredentials).build();

    if (credentials.isTokenExpired()) {
      var tokenDirectory = new File(GoogleApiUtilities.getTokenDirectoryPath());

      // Delete the old stored token
      if (tokenDirectory.exists() && tokenDirectory.isDirectory()) {
        for (var file : requireNonNull(tokenDirectory.listFiles())) {
          if (file.isFile() && !file.delete()) {
            log.warn("Failed to delete file {}", file.getAbsolutePath());
          }
        }
      }
    }

    var newCredentials = getCredentials(httpTransport);
    var user = getAuthenticatedUser(httpTransport, newCredentials);
    return AuthDetails.builder()
        .user(user)
        .httpTransport(httpTransport)
        .credential(newCredentials)
        .build();
  }

  private User getAuthenticatedUser(NetHttpTransport httpTransport, Credential credential)
      throws IOException {
    var driveService =
        new Drive.Builder(httpTransport, GoogleApiUtilities.getJsonFactory(), credential)
            .setApplicationName(GoogleApiUtilities.getApplicationName())
            .build();

    var about =
        driveService
            .about()
            .get()
            .setFields("user(displayName,emailAddress,photoLink,permissionId)")
            .execute();
    var userInfo = about.getUser();
    return userMapper.toDomain(userInfo);
  }

  @Builder
  public record AuthDetails(User user, NetHttpTransport httpTransport, Credential credential) {
    public boolean isTokenExpired() {
      var expiresInSeconds = credential.getExpiresInSeconds();
      return expiresInSeconds == null || expiresInSeconds <= 0;
    }
  }
}
