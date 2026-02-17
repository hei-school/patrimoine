package school.hei.patrimoine.modele.recouppement.generateur.correction;

import java.util.Set;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;

public class RecoupeurDePossessionFacade {
  private RecoupeurDePossessionFacade() {}

  @SuppressWarnings("all")
  public static <T extends Possession> PossessionRecoupee<T> recouper(
      Info<T> prevu, Set<Info<T>> realises) {
    var info = realises.stream().findFirst().orElse(prevu);
    var recoupeur = getRecoupeur(info.possession());
    return (PossessionRecoupee<T>) recoupeur.recouper(prevu, realises);
  }

  @SuppressWarnings("all")
  private static <T extends Possession> RecoupeurDePossession<T> getRecoupeur(T possession) {
    var recoupeur =
        switch (possession) {
          case FluxArgent ignored -> new RecoupeurDePossessionBase<>();
          default -> new NotSupportedRecoupeurDePossession();
        };

    return (RecoupeurDePossession<T>) recoupeur;
  }
}
