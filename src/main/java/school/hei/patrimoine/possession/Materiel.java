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
    Duration intervalD = Duration.between(this.t,tFutur);
    long intervalJ=intervalD.toDays();
    long intervalA = intervalJ / 365;
    double vComptableFuture = this.valeurComptable*(Math.pow(1+this.tauxDAppreciationAnnuelle,intervalA));
    return (int) Math.round(vComptableFuture);
  }
  @Override

  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
