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

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long nombre_d_annees = ChronoUnit.YEARS.between(getT(), tFutur);
    int valeurComptableFuture = (int) (getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, nombre_d_annees));
    return new Materiel(getNom(), tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
