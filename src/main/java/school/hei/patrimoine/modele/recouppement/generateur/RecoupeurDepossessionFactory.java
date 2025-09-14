package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.EXECUTE_SANS_CORRECTION;

public class RecoupeurDepossessionFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> RecoupeurDePossession<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (RecoupeurDePossession<T>) new FluxArgentRecoupeurDePossession();
    }

    return new RecoupeurDePossessionBase<>() {
      @Override
      public PossessionRecoupee comparer(T prévu, T réalisé) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, prévu, Set.of());
      }

      @Override
      public PossessionRecoupee nonÉxecuté(T nonÉxecuté) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, nonÉxecuté, Set.of());
      }

      @Override
      public PossessionRecoupee imprévu(T imprévu) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, imprévu, Set.of());
      }
    };
  }
}
