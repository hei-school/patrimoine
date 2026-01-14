package school.hei.patrimoine.patrilang.generator.possession;

import lombok.ToString;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TypeFEC;
import school.hei.patrimoine.patrilang.generator.*;

import static school.hei.patrimoine.modele.possession.TypeFEC.*;

@ToString
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
      if (fluxArgent.getTypeFEC() != null) {
        return sortir_type(fluxArgent);
      }
      return sortir(fluxArgent);
    } else if (fluxArgent.getTypeFEC() != null) {
      return entrer_type(fluxArgent);
    }

    return entrer(fluxArgent);
  }

  private TypeFEC getTypeFECString(String s) {
    var raw = s.trim().toUpperCase();
    return switch (raw) {
      case "Charge", "CHG" -> CHARGE;
      case "Produit", "PRD" -> PRODUIT;
      case "Immobilisation", "IMMO" -> IMMOBILISATION;
      case "CCA" -> CCA;
      default -> AUTRE;
    };
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

  private String sortir_type(FluxArgent fluxArgent) {
    var alias = getTypeFECString(fluxArgent.getTypeFEC().name());
    var nomTypé = idGenerator.apply("[" + alias + "]_" + fluxArgent.nom());
    var date = dateGenerator.apply(fluxArgent.t());
    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel().mult(-1));
    var type = variableTypeGenerator.apply(fluxArgent.getCompte());
    var compte = fluxArgent.getCompte().nom();

    return String.format("* `%s`, %s sortir %s depuis %s:%s", nomTypé, date, argent, type, compte);
  }

  private String entrer_type(FluxArgent fluxArgent) {
    var alias = getTypeFECString(fluxArgent.getTypeFEC().name());
    var nomTypé = "[" + alias + "]_" + fluxArgent.nom();
    var compte = fluxArgent.getCompte().nom();
    var date = dateGenerator.apply(fluxArgent.t());
    var argent = argentGenerator.apply(fluxArgent.getFluxMensuel());
    var type = variableTypeGenerator.apply(fluxArgent.getCompte());

    return String.format("* `%s`, %s entrer %s vers %s:%s", nomTypé, date, argent, type, compte);
  }
}
