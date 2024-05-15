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
    long nombreDeJour = Duration.between(this.t, tFutur).toDays();

    double tauxParJour = this.tauxDAppreciationAnnuelle / 365;
    double valeur = this.valeurComptable;
    for (int j = 0; j < nombreDeJour; j++) {
      valeur += valeur * tauxParJour;
    }

    Materiel materiel = new Materiel(
            this.nom,
            tFutur,
            (int) valeur,
            this.tauxDAppreciationAnnuelle
    );

    return materiel;
  }
}
