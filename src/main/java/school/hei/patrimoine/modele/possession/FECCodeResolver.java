package school.hei.patrimoine.modele.possession;

import lombok.NoArgsConstructor;
import school.hei.patrimoine.modele.possession.enumFEC.RegionComptable;
import school.hei.patrimoine.modele.possession.enumFEC.codeFEC;

@NoArgsConstructor
public class FECCodeResolver {
  public static String resolve(TypeFEC type, RegionComptable region) {
    return switch (type) {
      case CCA ->
          region == RegionComptable.MADAGASCAR ? codeFEC.CCA_MG.code() : codeFEC.CCA_FR.code();

      case PRODUIT -> codeFEC.PRODUIT.code();
      case CHARGE -> codeFEC.CHARGE.code();
      case IMMOBILISATION -> codeFEC.IMMOBILISATION.code();

      default -> codeFEC.AUTRE.code();
    };
  }
}
