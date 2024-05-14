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
  public int valeurComptableFuture(Instant tFutur) {
    double differenceEnAnnees = Duration.between(getT(), tFutur).toDays() / 365.0;
    double futureValue = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle / 100, differenceEnAnnees);
    return (int) futureValue;

  }
}