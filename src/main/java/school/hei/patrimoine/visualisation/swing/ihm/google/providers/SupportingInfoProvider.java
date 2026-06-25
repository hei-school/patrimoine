package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getSupportingInfoFile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class SupportingInfoProvider
    implements Function<PatriLangFileContext, SupportingInfoProviderResult> {
  @Override
  public SupportingInfoProviderResult apply(PatriLangFileContext casFile) {
    var optionalPjFile = getSupportingInfoFile(casFile);
    if (optionalPjFile.isEmpty()) {
      return SupportingInfoProviderResult.empty();
    }
    var pjFile = optionalPjFile.get();
    try {
      Map<String, PieceJustificative> map = new HashMap<>();
      for (var pj : transpilePieceJustificative(pjFile)) {
        map.put(pj.id(), pj);
      }
      var comments = transpileComments(pjFile);
      return new SupportingInfoProviderResult(map, comments);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
