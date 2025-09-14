package school.hei.patrimoine.modele.recouppement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

@Builder
public record PossessionRecoupee(
    PossessionRecoupeeStatus status,
    Possession possession,
    Set<Correction> corrections,
    Argent valeurPrevu,
    Argent valeurRealise,
    LocalDate datePrevu,
    LocalDate dateRealise) {
  public PossessionRecoupee(
      PossessionRecoupeeStatus status, Possession possession, Set<Correction> corrections) {
    this(
        status,
        possession,
        corrections,
        possession.valeurComptable(),
        possession.valeurComptable(),
        possession.t(),
        possession.t());
  }

  public int dateDifferenceEnJour() {
    if (LocalDate.MIN.equals(datePrevu)
        || LocalDate.MIN.equals(dateRealise)
        || LocalDate.MAX.equals(datePrevu)
        || LocalDate.MAX.equals(dateRealise)) {
      return 0;
    }

    return (int) ChronoUnit.DAYS.between(datePrevu, dateRealise);
  }

  public Argent valeurDifference() {
    return valeurPrevu.minus(valeurRealise, dateRealise);
  }

  public enum PossessionRecoupeeStatus {
    IMPREVU,
    NON_EXECUTE,
    EXECUTE_AVEC_CORRECTION,
    EXECUTE_SANS_CORRECTION
  }
}
