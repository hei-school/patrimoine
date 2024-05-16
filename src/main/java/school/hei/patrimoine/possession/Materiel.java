package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  public int valeurComptableFuture(Instant tFutur) {
    long monthsBetween = tFutur.toEpochMilli() - t.toEpochMilli();
    double depreciation = Math.pow(tauxDAppreciationAnnuelle, monthsBetween / (30.0 * 24 * 60 * 60 * 1000));

    return (int) (valeurComptable / depreciation);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
