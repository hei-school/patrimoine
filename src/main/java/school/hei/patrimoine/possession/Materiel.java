package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long differenceEntreTetFutureT= ChronoUnit.DAYS.between(getT(),tFutur)/365;
    double futureValeur=valeurComptable+(valeurComptable*
            (tauxDAppreciationAnnuelle*differenceEntreTetFutureT));
    return new Materiel(nom,t,(int) futureValeur,tauxDAppreciationAnnuelle);
  }
}
