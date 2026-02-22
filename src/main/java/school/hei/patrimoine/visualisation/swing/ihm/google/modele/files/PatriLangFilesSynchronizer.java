package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.driveApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;

import school.hei.patrimoine.google.exception.GoogleIntegrationException;

public class PatriLangFilesSynchronizer {
  private static final String MIME_TYPE = "application/octet-stream";

  public static void sync() {
    for (var input : getAllModifiedFiles()) {
      var file = (PatriLangFileContext) input.file();
      try {
        driveApi().update(file.getDriveId(), MIME_TYPE, file);
        PatriLangStagingFileManager.unstage(file);
      } catch (GoogleIntegrationException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
