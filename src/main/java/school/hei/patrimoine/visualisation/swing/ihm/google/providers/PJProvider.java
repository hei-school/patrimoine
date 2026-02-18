package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPatriLangJustificativeFiles;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class PJProvider implements Function<File, Map<String, PieceJustificative>> {
  @Override
  public Map<String, PieceJustificative> apply(File casFile) {
    var casName = casFile.getName();
    var baseName = casName.substring(0, casName.length() - CAS_FILE_EXTENSION.length());
    var optionalPjFile =
        getPatriLangJustificativeFiles().stream()
            .filter(file -> file.getName().equals(baseName + PJ_FILE_EXTENSION))
            .findFirst();

    if (optionalPjFile.isEmpty()) {
      return Map.of();
    }

    var pjFile = optionalPjFile.get();
    try {
      var pjs = transpilePieceJustificative(pjFile.getAbsolutePath());
      Map<String, PieceJustificative> map = new HashMap<>();
      for (var pj : pjs) {
        map.put(pj.id(), pj);
      }
      return map;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
