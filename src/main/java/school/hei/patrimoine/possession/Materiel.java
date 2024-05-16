package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle){
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    long differenceJour = ChronoUnit.DAYS.between(t, tFutur);
    long anneeApprox = differenceJour / 365;
    double futureValue = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, anneeApprox);
    return (int) futureValue;
  }
}
