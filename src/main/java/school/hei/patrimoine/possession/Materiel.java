package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  @Override
  public Possession projectionFuture(Instant tFutur) {
    long differenceJour = ChronoUnit.DAYS.between(getT(), tFutur);
    long anneeApprox = differenceJour / 365;
    double futureValue = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, anneeApprox);
    Materiel materiel = new Materiel(getNom(), tFutur, (int) futureValue, tauxDAppreciationAnnuelle);
    return materiel;
  }
}