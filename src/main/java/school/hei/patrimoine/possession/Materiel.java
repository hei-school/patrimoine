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
  public Materiel projectionFuture(Instant tFutur) {
    float nombreDeJoursEntreTEtTFuturEnAnnee = ChronoUnit.DAYS.between(t, tFutur) / 365;
    int valeurComptableFutur = (int) (valeurComptable + (valeurComptable * nombreDeJoursEntreTEtTFuturEnAnnee * tauxDAppreciationAnnuelle));
    Materiel materielFutur = new Materiel(nom, tFutur, valeurComptableFutur, tauxDAppreciationAnnuelle);
    return materielFutur;
  }
}
