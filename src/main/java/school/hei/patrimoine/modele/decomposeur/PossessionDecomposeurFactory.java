package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionDecomposeurFactory {
  @SuppressWarnings("unchecked")
  public static <ToDecompose extends Possession, Decomposed extends Possession>
  PossessionDecomposeur<ToDecompose, Decomposed> make(ToDecompose possession,LocalDate debut, LocalDate fin) {
    if (possession instanceof FluxArgent) {
      return (PossessionDecomposeur<ToDecompose, Decomposed>)
          new FluxArgentDecomposeur(debut, fin);
    }

    if (possession instanceof GroupePossession) {
      return (PossessionDecomposeur<ToDecompose, Decomposed>)
          new GroupePossessionDecomposeur(debut, fin);
    }

    return new PossessionDecomposeurBase<>(debut, fin) {
      @Override
      public List<Decomposed> apply(ToDecompose toDecompose) {
        if(isOutOfRange(toDecompose)) {
          return List.of();
        }

        return List.of((Decomposed) toDecompose);
      }
    };
  }
}
