package school.hei.patrimoine.google.api;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;
import static com.google.api.services.docs.v1.DocsScopes.DOCUMENTS_READONLY;
import static com.google.api.services.drive.DriveScopes.DRIVE;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.compiler.CompilerUtilities.USER_HOME;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import java.io.*;
import java.util.List;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.mapper.UserMapper;
import school.hei.patrimoine.google.model.User;

@Slf4j
public class GoogleApi {
  private final UserMapper userMapper;

  static final String APPLICATION_NAME = "patrimoine";
  static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static final List<String> SCOPES = List.of(DOCUMENTS_READONLY, DRIVE);
  private static final String TOKENS_DIRECTORY_PATH =
      USER_HOME + "/.patrimoine-ricka/google/tokens";
  private static final String CREDENTIALS_FILE_PATH =
      USER_HOME + "/.patrimoine-ricka/google/client.json";

  public GoogleApi() {
    this.userMapper = UserMapper.getInstance();
  }

  static {
    var tokensDirectory = new File(TOKENS_DIRECTORY_PATH);
    if (!tokensDirectory.exists() && !tokensDirectory.mkdirs()) {
      log.warn("Failed to create directory {}", tokensDirectory.getAbsolutePath());
    }
  }

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    var in = new FileInputStream(CREDENTIALS_FILE_PATH);
    var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization
    var flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
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
      var tokenDirectory = new File(TOKENS_DIRECTORY_PATH);

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
        new Drive.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
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
