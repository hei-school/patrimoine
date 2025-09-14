package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.ArgentPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.DatePatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;

public class FluxArgentPatriLangGenerator implements PatriLangGenerator<FluxArgent> {
  private final DatePatriLangGenerator datePatriLangGenerator;
  private final ArgentPatriLangGenerator argentPatriLangGenerator;

  public FluxArgentPatriLangGenerator() {
    this.datePatriLangGenerator = new DatePatriLangGenerator();
    this.argentPatriLangGenerator = new ArgentPatriLangGenerator();
  }

  @Override
  public String apply(FluxArgent fluxArgent) {
    if (fluxArgent.getFluxMensuel().lt(0)) {
      return sortir(fluxArgent);
    }

    return entrer(fluxArgent);
  }

  private String sortir(FluxArgent fluxArgent) {
    var nom = fluxArgent.nom();
    var compte = fluxArgent.getCompte().nom();
    var date = datePatriLangGenerator.apply(fluxArgent.t());
    var argent = argentPatriLangGenerator.apply(fluxArgent.getFluxMensuel().mult(-1));

    return String.format("* `%s`, %s sortir %s depuis Trésoreries:%s", nom, date, argent, compte);
  }

  private String entrer(FluxArgent fluxArgent) {
    var nom = fluxArgent.nom();
    var compte = fluxArgent.getCompte().nom();
    var date = datePatriLangGenerator.apply(fluxArgent.t());
    var argent = argentPatriLangGenerator.apply(fluxArgent.getFluxMensuel());

    return String.format("* `%s`, %s entrer %s vers Trésoreries:%s", nom, date, argent, compte);
  }
}
