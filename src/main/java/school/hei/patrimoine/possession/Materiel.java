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
    long differenceEnJour = Duration.between(t, tFutur).toDays();
    double differenceEnAnnees = differenceEnJour / 365;
    double futureValeur = valeurComptable + (tauxDAppreciationAnnuelle * differenceEnAnnees);
    return (int) futureValeur;
  }
}
