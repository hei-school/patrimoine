package school.hei.patrimoine.modele.calculation;

import school.hei.patrimoine.modele.possession.TypeAgregat;

public class ValeurCalculation {
  // stop instanciation and calculation for each type of possession
  public static ValeurMarcheCase getCalculation(TypeAgregat typeAgregat) {
    return switch (typeAgregat) {
      case IMMOBILISATION, ENTREPRISE -> new ValeurMarcheHistorique();
      default -> new ValeurComptable();
    };
  }
}
