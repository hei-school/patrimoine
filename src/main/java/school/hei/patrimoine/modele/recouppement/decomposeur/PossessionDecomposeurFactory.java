package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionDecomposeurFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> PossessionDecomposeur<T> make(
      T possession, LocalDate finSimulation) {
    if (possession instanceof FluxArgent) {
      return (PossessionDecomposeur<T>) new FluxArgentDecomposeur(finSimulation);
    }

    return new PossessionDecomposeurBase<>(finSimulation);
  }
}
