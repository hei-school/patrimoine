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

  public double getTauxDAppreciationAnnuelle() {
    return tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {

    throw new NotImplemented();
    Duration duration = Duration.between(t, tFutur);
    long differenceEnJours = duration.toDays();
    double tauxDeDepreciation = Math.pow(1 - tauxDAppreciationAnnuelle, differenceEnJours / 365.0);
    double valeurFuture = getValeurComptable() * tauxDeDepreciation;
    return (int) Math.round(valeurFuture);
  }
}
