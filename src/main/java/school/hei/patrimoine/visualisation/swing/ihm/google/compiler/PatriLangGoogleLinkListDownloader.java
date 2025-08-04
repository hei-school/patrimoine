package school.hei.patrimoine.visualisation.swing.ihm.google.compiler;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.compiler.PatriLangFileNameExtractor;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

@RequiredArgsConstructor
public class PatriLangGoogleLinkListDownloader implements GoogleLinkListDownloader {
  private final File driveDirectory;
  private final GoogleApi googleApi;
  private final GoogleApi.GoogleAuthenticationDetails authDetails;

  @Override
  public GoogleLinkList<NamedID> apply(GoogleLinkList<NamedID> ids) {
    for (var id : ids.docsLinkList()) {
      downloadDocsLink(id);
    }

    for (var namedId : ids.driveLinkList()) {
      googleApi.downloadDriveFile(authDetails, new PatriLangFileNameExtractor(), namedId.id());
    }

    return ids;
  }

  private void downloadDocsLink(NamedID namedID) {
    var code = googleApi.readDocsContent(authDetails, String.valueOf(namedID.id()));
    var filename = new PatriLangFileNameExtractor().apply(code);

    Path path = driveDirectory.toPath().resolve(filename);

    try {
      Files.write(path, code.getBytes(), CREATE, TRUNCATE_EXISTING, WRITE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
