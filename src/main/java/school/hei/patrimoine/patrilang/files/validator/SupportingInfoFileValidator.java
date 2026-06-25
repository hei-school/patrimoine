package school.hei.patrimoine.patrilang.files.validator;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpilePieceJustificative;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

public class SupportingInfoFileValidator implements Consumer<PatriLangFile> {
  @Override
  public void accept(PatriLangFile pj) {
    var pieces = transpilePieceJustificative(pj);
    validateNoDuplicateId(pieces);
  }

  private void validateNoDuplicateId(List<PieceJustificative> pieces) {
    Set<String> seenIds = new HashSet<>();
    for (var pj : pieces) {
      if (!seenIds.add(pj.id())) {
        throw new IllegalArgumentException(
            "Une opération ne devrait avoir qu'une seule pj."
                + " La pièce justificative \""
                + pj.id()
                + "\" existe déjà");
      }
    }
  }
}
