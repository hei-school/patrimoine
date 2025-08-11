package school.hei.patrimoine.visualisation.swing.ihm.google.downloader;

import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.compiler.PatriLangFileNameExtractor;
import school.hei.patrimoine.google.DocsApi;
import school.hei.patrimoine.google.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public record PatriLangGoogleLinkListDownloader(
    FileNameExtractor fileNameExtractor, DriveApi driveApi, DocsApi docsApi)
    implements GoogleLinkListDownloader {
  public PatriLangGoogleLinkListDownloader(DriveApi driveApi, DocsApi docsApi) {
    this(new PatriLangFileNameExtractor(), driveApi, docsApi);
  }

  @Override
  public GoogleLinkList<NamedID> apply(GoogleLinkList<NamedID> ids)
      throws GoogleIntegrationException {
    for (var nameId : ids.docsLinkList()) {
      docsApi.download(nameId.id(), fileNameExtractor);
    }

    for (var namedId : ids.driveLinkList()) {
      driveApi.download(namedId.id(), fileNameExtractor);
    }

    return ids;
  }
}
