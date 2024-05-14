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
    long duréeEnAnnée = Duration.between(t, tFutur).toDays() / 365;
    double facteurDappréciation = 1;
    for (int i = 0; i < duréeEnAnnée; i++) {
      facteurDappréciation *= (1 + tauxDAppreciationAnnuelle);
    }
    return (int) Math.round(valeurComptable * facteurDappréciation);
  }
}
