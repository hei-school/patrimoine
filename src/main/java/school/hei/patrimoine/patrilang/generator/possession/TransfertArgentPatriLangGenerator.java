package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.patrilang.generator.*;

public class TransfertArgentPatriLangGenerator implements PatriLangGenerator<TransfertArgent> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;
  private final ArgentPatriLangGenerator argentGenerator;
  private final ComptePatriLangGenerator compteGenerator;

  public TransfertArgentPatriLangGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
    this.argentGenerator = new ArgentPatriLangGenerator();
    this.compteGenerator = new ComptePatriLangGenerator();
  }

  @Override
  public String apply(TransfertArgent transfertArgent) {
    var nom = idGenerator.apply(transfertArgent.nom());
    var date = dateGenerator.apply(transfertArgent.t());
    var argent = argentGenerator.apply(transfertArgent.getFluxMensuel());
    var depuisCompte = compteGenerator.apply(transfertArgent.getDepuisCompte());
    var versCompte = compteGenerator.apply(transfertArgent.getVersCompte());

    return String.format(
        "* `%s`, %s transférer %s depuis %s vers %s", nom, date, argent, depuisCompte, versCompte);
  }
}
