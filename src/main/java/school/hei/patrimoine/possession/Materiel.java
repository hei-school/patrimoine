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
  public int valeurComptableFuture(Instant tFutur) {
    int diffDay = (int) ChronoUnit.DAYS.between(t, tFutur);
    int tauxByDay = (int) Math.abs(tauxDAppreciationAnnuelle * valeurComptable/100);
    return valeurComptable - (diffDay * tauxByDay);
  }
}
