package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long diffSeconds = Duration.between(this.t, tFutur).getSeconds();
    long diffDays = diffSeconds / (24 * 60 * 60); // Convert seconds to days
    double depreciationRatePerDay = this.tauxDAppreciationAnnuelle / 365.0;
    double totalDepreciation = depreciationRatePerDay * diffDays * this.valeurComptable / 100;
    int newValeurComptable = (int) (this.valeurComptable - totalDepreciation);

    newValeurComptable = Math.max(newValeurComptable, 0);

    return new Materiel(this.nom, tFutur, newValeurComptable, this.tauxDAppreciationAnnuelle);
  }
}
