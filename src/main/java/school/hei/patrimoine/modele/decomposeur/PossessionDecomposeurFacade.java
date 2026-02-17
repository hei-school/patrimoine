package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionDecomposeurFacade {
  @SuppressWarnings("all")
  public static <ToDecompose extends Possession, Decomposed extends Possession>
      List<Decomposed> decompose(ToDecompose possession, LocalDate debut, LocalDate fin) {
    var decomposeur = getDecomposeur(possession, debut, fin);
    return (List<Decomposed>) decomposeur.apply(possession);
  }

  @SuppressWarnings("all")
  private static <ToDecompose extends Possession, Decomposed extends Possession>
      PossessionDecomposeur<ToDecompose, Decomposed> getDecomposeur(
          ToDecompose possession, LocalDate debut, LocalDate fin) {
    var decomposeur =
        switch (possession) {
          case FluxArgent flux -> new FluxArgentDecomposeur(debut, fin);
          case GroupePossession groupe -> new GroupePossessionDecomposeur(debut, fin);
          default -> new PossessionDecomposeurBase<>(debut, fin);
        };

    return (PossessionDecomposeur<ToDecompose, Decomposed>) decomposeur;
  }
}
