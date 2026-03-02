package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.*;

public class FluxArgentPatriLangGenerator implements PatriLangGenerator<FluxArgent> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;
  private final ArgentPatriLangGenerator argentGenerator;
  private final VariableTypePatriLangGenerator variableTypeGenerator;

  public FluxArgentPatriLangGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
    this.argentGenerator = new ArgentPatriLangGenerator();
    this.variableTypeGenerator = new VariableTypePatriLangGenerator();
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
    var type = variableTypeGenerator.apply(fluxArgent.getCompte());
    var compte = fluxArgent.getCompte().nom();

    return String.format("* `%s`, %s sortir %s depuis %s:%s", nom, date, argent, type, compte);
  }

  private String entrer(FluxArgent fluxArgent) {
    var nom = fluxArgent.nom();
    var compte = fluxArgent.getCompte().nom();
    var date = dateGenerator.apply(fluxArgent.t());
    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel());
    var type = variableTypeGenerator.apply(fluxArgent.getCompte());

    return String.format("* `%s`, %s entrer %s vers %s:%s", nom, date, argent, type, compte);
  }
}
