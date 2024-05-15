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
    Instant lastT = getT();
    if (tFutur.isBefore(lastT)) {
      throw new RuntimeException();
    }

    var tInterval = Duration.between(lastT, tFutur).toDays();
    var tauxRecent = tauxDAppreciationAnnuelle / 365 * tInterval;
    var valeurRecent = (int) (getValeurComptable() + tauxRecent);

    return new Materiel(getNom(), tFutur, valeurRecent, tauxDAppreciationAnnuelle);

  }
}
