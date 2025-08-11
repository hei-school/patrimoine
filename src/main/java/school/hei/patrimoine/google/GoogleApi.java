package school.hei.patrimoine.google;

import static com.google.api.services.docs.v1.DocsScopes.DOCUMENTS_READONLY;
import static com.google.api.services.drive.DriveScopes.DRIVE;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.compiler.CompilerUtilities.USER_HOME;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.*;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleApi {
  static final String APPLICATION_NAME = "patrimoine";
  static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static final List<String> SCOPES = List.of(DOCUMENTS_READONLY, DRIVE);
  private static final String TOKENS_DIRECTORY_PATH = USER_HOME + "/.patrimoine/google/tokens";
  private static final String CREDENTIALS_FILE_PATH = USER_HOME + "/.patrimoine/google/client.json";

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
    var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    var currentCredentials = getCredentials(HTTP_TRANSPORT);
    var credentials = new AuthDetails(HTTP_TRANSPORT, currentCredentials);

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

    var newCredentials = getCredentials(HTTP_TRANSPORT);
    return new AuthDetails(HTTP_TRANSPORT, newCredentials);
  }

  public record AuthDetails(NetHttpTransport httpTransport, Credential credential) {
    public boolean isTokenExpired() {
      var expiresInSeconds = credential.getExpiresInSeconds();
      return expiresInSeconds == null || expiresInSeconds <= 0;
    }
  }
}
