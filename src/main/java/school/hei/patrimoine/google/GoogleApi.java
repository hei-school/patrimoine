package school.hei.patrimoine.google;

import static com.google.api.services.docs.v1.DocsScopes.DOCUMENTS_READONLY;
import static com.google.api.services.drive.DriveScopes.DRIVE_READONLY;

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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.services.drive.Drive;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.ClassNameExtractor;

@Slf4j
public class GoogleApi {
  public record GoogleAuthenticationDetails(NetHttpTransport httpTransport, Credential credential) {

    public boolean isTokenExpired() {
      Long expiresInSeconds = credential.getExpiresInSeconds();

      return expiresInSeconds == null || expiresInSeconds <= 0;
    }
  }

  private static final String APPLICATION_NAME = "patrimoine";

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /** Directory to store authorization tokens for this application. */
  private static final String TOKENS_DIRECTORY_PATH =
      System.getProperty("user.home") + "/.patrimoine/google/tokens";

  static {
    new File(TOKENS_DIRECTORY_PATH).mkdirs();
  }

  private static final List<String> SCOPES = List.of(DOCUMENTS_READONLY, DRIVE_READONLY);
  private static final String CREDENTIALS_FILE_PATH =
      System.getProperty("user.home") + "/.patrimoine/google/client.json";

  public static final String DOWNLOADS_DIRECTORY_PATH =
          System.getProperty("user.home") + "/Downloads/drive";

  static {
    createOrResetDirectory(DOWNLOADS_DIRECTORY_PATH);
  }

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
    final var currentCredentials = getCredentials(HTTP_TRANSPORT);
    var creds = new GoogleAuthenticationDetails(HTTP_TRANSPORT, currentCredentials);
    if (creds.isTokenExpired()) {
      File tokenDirectory = new File(TOKENS_DIRECTORY_PATH);
      if (tokenDirectory.exists() && tokenDirectory.isDirectory()) {
        for (File file : tokenDirectory.listFiles()) {
          if (file.isFile()) {
            file.delete();
          }
        }
      }
    }
    final var newCredentials = getCredentials(HTTP_TRANSPORT);
    return new GoogleAuthenticationDetails(HTTP_TRANSPORT, newCredentials);
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

  public static void createOrResetDirectory(String directoryPath) {
    Path path = Paths.get(directoryPath);
    File directory = path.toFile();

    try {
      // Deletes existing directory if present
      if (directory.exists()) {
        Files.walk(path)
                .map(Path::toFile)
                .forEach(File::delete);
      }
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("Directory reset error : " + directoryPath, e);
    }
  }

  public void downloadFile(GoogleAuthenticationDetails authDetails, String fileId) {
    Drive driveService = new Drive.Builder(authDetails.httpTransport(), JSON_FACTORY, authDetails.credential())
            .setApplicationName(APPLICATION_NAME)
            .build();

    // Create a temporary file to store downloaded content
    File tempFile = new File(System.getProperty("java.io.tmpdir"), "temp_download.java");

    try (InputStream inputStream = driveService.files().get(fileId).executeMediaAsInputStream();
         FileOutputStream tempOutputStream = new FileOutputStream(tempFile)) {

      // Download content to temporary file
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        tempOutputStream.write(buffer, 0, bytesRead);
      }

      // Read the contents of the temporary file as a String
      String fileContent = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);

      // Extract class name
      String className = new ClassNameExtractor().apply(fileContent);
      if (className == null || className.isEmpty()) {
        throw new RuntimeException("The class name could not be extracted");
      }

      // Define final file name based on class name
      File finalFile = new File(DOWNLOADS_DIRECTORY_PATH, className + ".java");

      try (FileOutputStream finalOutputStream = new FileOutputStream(finalFile);
           FileInputStream tempInputStream = new FileInputStream(tempFile)) {

        while ((bytesRead = tempInputStream.read(buffer)) != -1) {
          finalOutputStream.write(buffer, 0, bytesRead);
        }

      } finally {
        if (tempFile.exists()) {
          tempFile.delete();
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
