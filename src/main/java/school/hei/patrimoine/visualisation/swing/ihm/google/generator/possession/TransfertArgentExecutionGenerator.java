package school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession;

import static school.hei.patrimoine.visualisation.swing.ihm.google.generator.PossessionGeneratorFactory.MULTIPLE_EXECUTION_NOM_FORMAT;

import java.time.LocalDate;
import java.util.Map;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

public class TransfertArgentExecutionGenerator implements ExecutionGenerator<TransfertArgent> {
  @Override
  public void validateArgs(Map<String, Object> args) throws IllegalArgumentException {
    if (!args.containsKey("nom")) {
      throw new IllegalArgumentException("nom is mandatory to create a fluxArgent");
    }

    if (!args.containsKey("date")) {
      throw new IllegalArgumentException("date is mandatory to create a fluxArgent");
    }

    if (!args.containsKey("valeur")) {
      throw new IllegalArgumentException("valeur is mandatory to create a fluxArgent");
    }

    if (!args.containsKey("depuisCompte")) {
      throw new IllegalArgumentException("depuisCompte is mandatory to create a fluxArgent");
    }

    if (!args.containsKey("versCompte")) {
      throw new IllegalArgumentException("versCompte is mandatory to create a fluxArgent");
    }
  }

  @Override
  public TransfertArgent apply(Map<String, Object> args) {
    validateArgs(args);

    var nom = (String) args.get("nom");
    var prevu = (TransfertArgent) args.getOrDefault("prévu", null);
    var realisationNom =
        prevu == null ? nom : String.format(MULTIPLE_EXECUTION_NOM_FORMAT, prevu.nom(), nom);

    return new TransfertArgent(
        realisationNom,
        (Compte) args.get("depuisCompte"),
        (Compte) args.get("versCompte"),
        (LocalDate) args.get("date"),
        (Argent) args.get("valeur"));
  }
}
