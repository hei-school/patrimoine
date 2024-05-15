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
    long jourTotalEntreAchatDateEttFutur = Duration.between(this.t, tFutur).toDays();
    double anneesTotales = (double) jourTotalEntreAchatDateEttFutur / 365;
    double valeurFuture = this.valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, anneesTotales);
    return new Materiel(this.nom, tFutur, (int) valeurFuture, this.tauxDAppreciationAnnuelle);
  }
}
