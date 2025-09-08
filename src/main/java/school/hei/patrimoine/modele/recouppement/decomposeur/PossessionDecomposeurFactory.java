package school.hei.patrimoine.modele.recouppement.decomposeur;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionDecomposeurFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> PossessionDecomposeur<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (PossessionDecomposeur<T>) new FluxArgentDecomposeur();
    }

    return new PossessionDecomposeurBase<>();
  }
}
