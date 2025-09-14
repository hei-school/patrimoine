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
    var nom = fluxArgent.nom();
    var compte = fluxArgent.getCompte().nom();
    var date = datePatriLangGenerator.apply(fluxArgent.t());
    var argent = argentPatriLangGenerator.apply(fluxArgent.getFluxMensuel().mult(-1));

    if (fluxArgent.getFluxMensuel().lt(0)) {
      return sortir(nom, date, argent, compte);
    }

    return entrer(nom, date, argent, compte);
  }

  private String sortir(String nom, String date, String argent, String compte) {
    return String.format("* `%s`, %s sortir %s depuis Trésoreries:%s", nom, date, argent, compte);
  }

  private String entrer(String nom, String date, String argent, String compte) {
    return String.format("* `%s`, %s entrer %s vers Trésoreries:%s", nom, date, argent, compte);
  }
}
