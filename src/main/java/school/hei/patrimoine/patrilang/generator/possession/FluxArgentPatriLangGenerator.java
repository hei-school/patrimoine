package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.*;

public class FluxArgentPatriLangGenerator implements PatriLangGenerator<FluxArgent> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;
  private final ArgentPatriLangGenerator argentGenerator;
  private final VariableTypePatriLangGenerator variableTypeGenerator;

  private static final String SEPARATEUR_ID = "_";

  public FluxArgentPatriLangGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
    this.argentGenerator = new ArgentPatriLangGenerator();
    this.variableTypeGenerator = new VariableTypePatriLangGenerator();
  }

  @Override
  public String apply(FluxArgent fluxArgent) {
    var isSortie = fluxArgent.getFluxMensuel().lt(0);

    var nom = buildNom(fluxArgent);
    var date = dateGenerator.apply(fluxArgent.t());
    var type = variableTypeGenerator.apply(fluxArgent.getCompte());
    var compte = fluxArgent.getCompte().nom();

    if (isSortie) {
      var argent = argentGenerator.apply(fluxArgent.getFluxMensuel().mult(-1));
      return String.format("* `%s`, %s sortir %s depuis %s:%s", nom, date, argent, type, compte);
    }

    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel());
    return String.format("* `%s`, %s entrer %s vers %s:%s", nom, date, argent, type, compte);
  }

  private String buildNom(FluxArgent fluxArgent) {
    if (fluxArgent.getTypeFEC() == null) {
      return idGenerator.apply(fluxArgent.nom());
    }

    var type = fluxArgent.getTypeFEC();
    var nomTypé = type + SEPARATEUR_ID + fluxArgent.nom();
    return idGenerator.apply(nomTypé);
  }
}
