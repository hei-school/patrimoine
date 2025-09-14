package school.hei.patrimoine.modele.recouppement.generateur;

import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.EXECUTE_SANS_CORRECTION;

import java.util.Set;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class RecoupeurDePossessionFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> RecoupeurDePossession<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (RecoupeurDePossession<T>) new FluxArgentRecoupeurDePossession();
    }

    return new RecoupeurDePossessionBase<>() {
      @Override
      public PossessionRecoupee comparer(T prevu, T realise) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, prevu, Set.of());
      }

      @Override
      public PossessionRecoupee nonExecute(T nonExecute) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, nonExecute, Set.of());
      }

      @Override
      public PossessionRecoupee imprevu(T imprevu) {
        return new PossessionRecoupee(EXECUTE_SANS_CORRECTION, imprevu, Set.of());
      }
    };
  }
}
