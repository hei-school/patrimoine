package school.hei.patrimoine.possession;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Materiel projectionFuture(Instant tFutur) {
    double jourDifference = Duration.between(t, tFutur).toDays();
    double anneesDifference = jourDifference / 365;

    int valeurComptableProjetee = (int) (valeurComptable * Math.exp(tauxDAppreciationAnnuelle * anneesDifference));
    return new Materiel(nom, tFutur, valeurComptableProjetee, tauxDAppreciationAnnuelle);
  }
}
