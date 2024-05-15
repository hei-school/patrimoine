package school.hei.patrimoine.possession;

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
    Instant lastT = getT();
    var tInterval = Duration.between(lastT, tFutur).toDays();

    if (tFutur.isBefore(lastT)) {
      throw new RuntimeException("Future t provided is before last t");
    }

    var ratePerDay = tauxDAppreciationAnnuelle / 365;
    var currentRate = ratePerDay * tInterval;
    var currentValue = (int) (getValeurComptable() + currentRate);

    return new Materiel(getNom(), tFutur, currentValue, tauxDAppreciationAnnuelle);
  }
}
