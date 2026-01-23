package school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.pj;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getPatriLangJustificativeFiles;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Slf4j
public class PJRetriever implements Function<File, Set<PieceJustificative>> {
  @Override
  public Set<PieceJustificative> apply(File casFile) {
    if (casFile == null) {
      return Set.of();
    }

    var casName = casFile.getName();
    var baseName = casName.substring(0, casName.length() - CAS_FILE_EXTENSION.length());
    var pjFile =
        getPatriLangJustificativeFiles().stream()
            .filter(file -> file.getName().equals(baseName + PJ_FILE_EXTENSION))
            .findFirst()
            .orElse(null);

    if (pjFile == null) {
      return Set.of();
    }

    try {
      return new HashSet<>(transpilePieceJustificative(pjFile.getAbsolutePath()));
    } catch (Exception e) {
      log.error("Failed to retrieve Piece Justificative files for Cas.name={}", baseName, e);
      return Set.of();
    }
  }
}
