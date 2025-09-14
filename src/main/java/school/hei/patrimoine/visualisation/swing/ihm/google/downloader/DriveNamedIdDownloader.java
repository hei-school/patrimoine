package school.hei.patrimoine.visualisation.swing.ihm.google.downloader;

import static school.hei.patrimoine.compiler.CompilerUtilities.*;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.compiler.PatriLangFileNameExtractor;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

@Slf4j
public record DriveNamedIdDownloader(FileNameExtractor fileNameExtractor, DriveApi driveApi)
    implements NamedIdDownloader {
  public static final String DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH =
      DOWNLOADS_DIRECTORY_PATH + "/planned";
  public static final String DOWNLOAD_DONE_FILE_DIRECTORY_PATH = DOWNLOADS_DIRECTORY_PATH + "/done";

  public DriveNamedIdDownloader(DriveApi driveApi) {
    this(new PatriLangFileNameExtractor(), driveApi);
  }

  static {
    var plannedFilesDirectory = new File(DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH);
    var doneFilesDirectory = new File(DOWNLOAD_DONE_FILE_DIRECTORY_PATH);

    if (!plannedFilesDirectory.exists() && !plannedFilesDirectory.mkdirs()) {
      log.warn("Failed to create directory {}", plannedFilesDirectory.getAbsolutePath());
    }

    if (!doneFilesDirectory.exists() && !plannedFilesDirectory.mkdirs()) {
      log.warn("Failed to create directory {}", plannedFilesDirectory.getAbsolutePath());
    }

    resetIfExist(DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH);
    resetIfExist(DOWNLOAD_DONE_FILE_DIRECTORY_PATH);
  }

  @Override
  public GoogleLinkList<NamedID> apply(GoogleLinkList<NamedID> ids)
      throws GoogleIntegrationException {
    for (var namedId : ids.planned()) {
      driveApi.download(namedId.id(), fileNameExtractor, DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH);
    }

    for (var namedId : ids.done()) {
      driveApi.download(namedId.id(), fileNameExtractor, DOWNLOAD_DONE_FILE_DIRECTORY_PATH);
    }

    return ids;
  }
}
