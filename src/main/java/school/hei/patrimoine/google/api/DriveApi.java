package school.hei.patrimoine.google.api;

import static java.util.UUID.randomUUID;
import static school.hei.patrimoine.google.api.GoogleApi.*;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;

@Slf4j
public record DriveApi(Drive driveService) {
  public DriveApi(AuthDetails authDetails) {
    this(
        new Drive.Builder(
                authDetails.httpTransport(),
                GoogleApiUtilities.getJsonFactory(),
                authDetails.credential())
            .setApplicationName(GoogleApiUtilities.getApplicationName())
            .build());
  }

  public void update(String driveFileId, String mimeType, File localFile)
      throws GoogleIntegrationException {
    try {
      var fileMetadata = new com.google.api.services.drive.model.File();
      fileMetadata.setName(localFile.getName());

      var mediaContent = new FileContent(mimeType, localFile);
      driveService.files().update(driveFileId, fileMetadata, mediaContent).execute();
    } catch (IOException e) {
      throw new GoogleIntegrationException(
          "Échec de la mise à jour du fichier sur Google Drive", e);
    }
  }

  public void download(String driveFileId, FileNameExtractor fileNameExtractor, String downloadPath)
      throws GoogleIntegrationException {
    var tempFile = new File(GoogleApiUtilities.getTempDirectory(), "prefix_" + randomUUID());

    try (var inputStream = driveService.files().get(driveFileId).executeMediaAsInputStream();
        var tempOutputStream = new FileOutputStream(tempFile)) {

      // Download content to a temporary file
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        tempOutputStream.write(buffer, 0, bytesRead);
      }

      var fileContent = Files.readString(tempFile.toPath());
      var filename = fileNameExtractor.apply(fileContent);

      var finalFile = new File(downloadPath, filename);

      copyFileContent(tempFile, finalFile);

    } catch (IOException e) {
      throw new GoogleIntegrationException(
          "Échec du téléchargement du fichier depuis Google Drive", e);
    }
  }

  public static void copyFileContent(File sourceFile, File targetFile) {
    byte[] buffer = new byte[1024];
    int bytesRead;

    try (FileOutputStream targetOutputStream = new FileOutputStream(targetFile);
        FileInputStream sourceInputStream = new FileInputStream(sourceFile)) {

      while ((bytesRead = sourceInputStream.read(buffer)) != -1) {
        targetOutputStream.write(buffer, 0, bytesRead);
      }

    } catch (IOException e) {
      throw new RuntimeException("Failed to copy file content", e);
    } finally {
      if (sourceFile.exists() && !sourceFile.delete()) {
        log.warn("Failed to delete file {}", sourceFile.getAbsolutePath());
      }
    }
  }
}
