package school.hei.patrimoine.modele.recouppement;

import java.util.Set;
import lombok.Builder;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

@Builder
public record PossessionRecoupee(
    PossessionRecoupeeStatus status,
    Possession possession,
    Set<Correction> corrections
) {
  public enum PossessionRecoupeeStatus {
    IMPREVU,
    NON_EXECUTE,
    EXECUTE_AVEC_CORRECTION,
    EXECUTE_SANS_CORRECTION
  }
}
