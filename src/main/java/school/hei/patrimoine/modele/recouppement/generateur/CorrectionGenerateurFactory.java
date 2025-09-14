package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

public class CorrectionGenerateurFactory {
  @SuppressWarnings("unchecked")
  public static <T extends Possession> CorrectionGenerateur<T> make(T possession) {
    if (possession instanceof FluxArgent) {
      return (CorrectionGenerateur<T>) new FluxArgentCorrectionGenerateur();
    }

    return new CorrectionGenerateurBase<>() {
      @Override
      public Set<Correction> comparer(T prévu, T réalité) {
        return Set.of();
      }

      @Override
      public Set<Correction> nonÉxecuté(T nonÉxecuté) {
        return Set.of();
      }

      @Override
      public Set<Correction> imprévu(T nonPrévu) {
        return Set.of();
      }
    };
  }
}
