package school.hei.patrimoine.compiler;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.google.GoogleApi;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

@RequiredArgsConstructor
public class PatriLangGoogleLinkListCompiler implements GoogleLinkListCompiler {
  private final File driveDirectory;
  private final GoogleApi googleApi;
  private final GoogleApi.GoogleAuthenticationDetails authDetails;

  @Override
  public List<Patrimoine> apply(GoogleLinkList<NamedID> ids) {
    for (var id : ids.docsLinkList()) {
      downloadDocsLink(id);
    }

    for (var namedId : ids.driveLinkList()) {
      googleApi.downloadDriveFile(authDetails, new PatriLangFileNameExtractor(), namedId.id());
    }

    var casSetFile =
        Arrays.stream(
                requireNonNull(
                    driveDirectory.listFiles(
                        (dir, name) -> name.endsWith(TOUT_CAS_FILE_EXTENSION))))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("Aucun fichier .tout.md n’a été trouvé."));
    var casSet = transpileToutCas(casSetFile.getAbsolutePath());

    new CasSetAnalyzer().accept(casSet);

    return List.of();
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
