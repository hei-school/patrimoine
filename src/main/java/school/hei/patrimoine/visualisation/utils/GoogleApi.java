package school.hei.patrimoine.visualisation.utils;

import static com.google.api.services.docs.v1.DocsScopes.DOCUMENTS_READONLY;

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
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.Paragraph;
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.google.api.services.docs.v1.model.TextRun;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.GoogleAuthScreen;

@Slf4j
public class GoogleApi {
  public record GoogleAuthenticationDetails(
      NetHttpTransport httpTransport, Credential credential) {}

  private static final String APPLICATION_NAME = "harena";

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /** Directory to store authorization tokens for this application. */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  private static final List<String> SCOPES = List.of(DOCUMENTS_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/harena-client.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = GoogleAuthScreen.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    // returns an authorized Credential object.
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  @SneakyThrows
  public GoogleAuthenticationDetails requestAuthentication() {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final var credentials = getCredentials(HTTP_TRANSPORT);
    return new GoogleAuthenticationDetails(HTTP_TRANSPORT, credentials);
  }

  public String readDocsContent(GoogleAuthenticationDetails authDetails, String docId) {
    Docs service =
        new Docs.Builder(authDetails.httpTransport(), JSON_FACTORY, authDetails.credential())
            .setApplicationName(APPLICATION_NAME)
            .build();
    try {
      Document doc = service.documents().get(docId).execute();
      var list =
          doc.getBody().getContent().stream()
              .map(StructuralElement::getParagraph)
              .filter(Objects::nonNull)
              .map(Paragraph::getElements)
              .flatMap(List::stream)
              .map(ParagraphElement::getTextRun)
              .map(TextRun::getContent)
              .toList();
      return String.join(" ", list);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    var api = new GoogleApi();
    var auth = api.requestAuthentication();
    var content = api.readDocsContent(auth, "1mI6dI4ubJboQIzvlhjXkoZm9XnCQSV0p0D-LYQra5NQ");
    log.info("content {}", content);
  }
}
