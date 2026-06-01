package school.hei.patrimoine.patrilang.generator.possession;

import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;

import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.generator.DatePatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.IdPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;

public class PieceJustificativePatriLangGenerator
    implements PatriLangGenerator<PieceJustificative> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;

  public PieceJustificativePatriLangGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
  }

  @Override
  public String apply(PieceJustificative pj) {
    var id = this.idGenerator.apply(pj.id());
    var date = this.dateGenerator.apply(pj.date());
    var ref = normalize(pj.reference());

    return String.format("* `%s`, %s, %s, \"%s\"", id, date, ref, pj.link());
  }
}
