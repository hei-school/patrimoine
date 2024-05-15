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

    int joursProjection = (int) Duration.between(this.t, tFutur).toDays();
    double valeurComptable = this.valeurComptable;
    double tauxDAppreciationJournalier = this.tauxDAppreciationAnnuelle / 365;

    for (int j = 0; j < joursProjection; j++) {
      valeurComptable += valeurComptable * tauxDAppreciationJournalier;
    }

    Materiel materiel = new Materiel(
            this.nom,
            tFutur,
            (int) valeurComptable,
            this.tauxDAppreciationAnnuelle
    );
    return materiel;
  }
}
