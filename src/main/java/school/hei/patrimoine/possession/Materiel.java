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
  public int valeurComptableFuture(Instant tFutur) {
    double valeurAjouteeAnnuelle = valeurComptable * tauxDAppreciationAnnuelle;
    long anneesDeDifference = Duration.between(t, tFutur).toDays() / 365;
    return (int) (valeurComptable + (valeurAjouteeAnnuelle * anneesDeDifference));
  }
}
