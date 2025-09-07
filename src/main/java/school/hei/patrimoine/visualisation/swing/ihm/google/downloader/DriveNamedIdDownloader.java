package school.hei.patrimoine.visualisation.swing.ihm.google.downloader;

import java.util.List;
import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.compiler.PatriLangFileNameExtractor;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public record DriveNamedIdDownloader(FileNameExtractor fileNameExtractor, DriveApi driveApi)
    implements NamedIdDownloader {
  public DriveNamedIdDownloader(DriveApi driveApi) {
    this(new PatriLangFileNameExtractor(), driveApi);
  }

  @Override
  public List<NamedID> apply(List<NamedID> ids) throws GoogleIntegrationException {

    for (var namedId : ids) {
      driveApi.download(namedId.id(), fileNameExtractor);
    }

    return ids;
  }
}
