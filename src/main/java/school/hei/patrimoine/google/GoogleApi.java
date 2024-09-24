package school.hei.patrimoine.google;

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
import com.google.api.services.docs.v1.model.ParagraphElement;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.google.api.services.docs.v1.model.TextRun;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleApi {
  public record GoogleAuthenticationDetails(
      NetHttpTransport httpTransport, Credential credential) {}

  private static final String APPLICATION_NAME = "patrimoine";

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /** Directory to store authorization tokens for this application. */
  private static final String TOKENS_DIRECTORY_PATH =
      System.getProperty("user.home") + "/.patrimoine/google/tokens";

  static {
    new File(TOKENS_DIRECTORY_PATH).mkdirs();
  }

  private static final List<String> SCOPES = List.of(DOCUMENTS_READONLY);
  private static final String CREDENTIALS_FILE_PATH =
      System.getProperty("user.home") + "/.patrimoine/google/client.json";

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
    InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
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
      List<StructuralElement> bodyElements = doc.getBody().getContent();

      StringBuilder combinedContent = new StringBuilder();

      // Iterate through the body elements
      for (StructuralElement element : bodyElements) {
        if (element.getParagraph() != null) {
          // For each paragraph, get the text content
          List<ParagraphElement> elements = element.getParagraph().getElements();
          for (ParagraphElement e : elements) {
            TextRun textRun = e.getTextRun();
            if (textRun != null && textRun.getContent() != null) {
              combinedContent.append(textRun.getContent());
            }
          }
        }
      }

      return combinedContent.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
