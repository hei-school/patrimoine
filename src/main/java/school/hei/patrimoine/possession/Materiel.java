package school.hei.patrimoine.possession;

import java.time.Duration;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Materiel projectionFuture(Instant tFutur) {
    long differenceEnMillis = Duration.between(t, tFutur).toMillis();

    long nombreAnnees = differenceEnMillis / (1000L * 60 * 60 * 24 * 365);

    double valeurComptableFuture = valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, nombreAnnees);

    return new Materiel(nom, tFutur, (int) Math.round(valeurComptableFuture), tauxDAppreciationAnnuelle);
  }

}
