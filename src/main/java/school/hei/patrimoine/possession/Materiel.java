package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  //1709089
  @Override
  public int valeurComptableFuture(Instant tFutur) {
    int ageMateriel = (int) Math.abs(ChronoUnit.DAYS.between(t, tFutur) / 365);
    int valeurFuture = valeurComptable;
    for (int i = 0; i < ageMateriel; i++) {
      valeurFuture -= (int) (valeurFuture * 0.1);
    }
    return valeurFuture;
  }
}
