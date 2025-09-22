package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

public class RecoupeurDePossessionFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> RecoupeurDePossession<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (RecoupeurDePossession<T>) new FluxArgentRecoupeurDePossession();
    }

    return new RecoupeurDePossessionBase<>();
  }
}
