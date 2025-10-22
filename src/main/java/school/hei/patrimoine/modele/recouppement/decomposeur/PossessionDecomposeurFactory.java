package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionDecomposeurFactory {
  @SuppressWarnings("unchecked")
  public static <ToDecompose extends Possession, Decomposed extends Possession>
      PossessionDecomposeur<ToDecompose, Decomposed> make(
          ToDecompose possession, LocalDate finSimulation) {

    if (possession instanceof FluxArgent) {
      return (PossessionDecomposeur<ToDecompose, Decomposed>)
          new FluxArgentDecomposeur(finSimulation);
    }

    if (possession instanceof GroupePossession) {
      return (PossessionDecomposeur<ToDecompose, Decomposed>)
          new GroupePossessionDecomposeur(finSimulation);
    }

    return new PossessionDecomposeurBase<>(finSimulation) {
      @Override
      public List<Decomposed> apply(ToDecompose toDecompose) {
        return List.of((Decomposed) toDecompose);
      }
    };
  }

  static String normalize(String nom) {
    return nom.replaceAll("-", "_");
  }
}
