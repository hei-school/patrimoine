package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPJ;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class PJProvider implements Function<PatriLangFileContext, Map<String, PieceJustificative>> {
  @Override
  public Map<String, PieceJustificative> apply(PatriLangFileContext casFile) {
    var optionalPjFile = getPJ(casFile);

    if (optionalPjFile.isEmpty()) {
      return Map.of();
    }

    var pjFile = optionalPjFile.get();
    try {
      Map<String, PieceJustificative> map = new HashMap<>();
      for (var pj : transpilePieceJustificative(pjFile)) {
        map.put(pj.id(), pj);
      }
      return map;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
