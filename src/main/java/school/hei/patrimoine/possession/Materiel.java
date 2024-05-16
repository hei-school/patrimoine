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
    Instant tLast = t;

    if (tFutur.isBefore(tLast)) {
      throw new RuntimeException("T future provided is before t last");
    }

    int tInterval = (int) Duration.between(tLast, tFutur).toDays();

    Double ratePerDay = tauxDAppreciationAnnuelle / 365;
    Double futureRate = ratePerDay * tInterval;

    int futureCountableValue = (int) Math.round(valeurComptable + (valeurComptable * futureRate));

    return new Materiel(nom, tFutur, futureCountableValue, tauxDAppreciationAnnuelle);
  }
}
