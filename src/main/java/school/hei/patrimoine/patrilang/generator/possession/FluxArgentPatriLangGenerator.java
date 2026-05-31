package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.*;

public class FluxArgentPatriLangGenerator implements PatriLangGenerator<FluxArgent> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;
  private final ComptePatriLangGenerator compteGenerator;
  private final ArgentPatriLangGenerator argentGenerator;

  public FluxArgentPatriLangGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
    this.argentGenerator = new ArgentPatriLangGenerator();
    this.compteGenerator = new ComptePatriLangGenerator();
  }

  @Override
  public String apply(FluxArgent fluxArgent) {
    if (fluxArgent.getFluxMensuel().lt(0)) {
      return sortir(fluxArgent);
    }

    return entrer(fluxArgent);
  }

  private String sortir(FluxArgent fluxArgent) {
    var nom = idGenerator.apply(fluxArgent.nom());
    var date = dateGenerator.apply(fluxArgent.t());
    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel().mult(-1));
    var compte = compteGenerator.apply(fluxArgent.getCompte());

    return String.format("* `%s`, %s sortir %s depuis %s", nom, date, argent, compte);
  }

  private String entrer(FluxArgent fluxArgent) {
    var nom = idGenerator.apply(fluxArgent.nom());
    var date = dateGenerator.apply(fluxArgent.t());
    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel());
    var compte = compteGenerator.apply(fluxArgent.getCompte());

    return String.format("* `%s`, %s entrer %s vers %s", nom, date, argent, compte);
  }
}
