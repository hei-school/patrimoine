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
  public int valeurComptableFuture(Instant tFutur) {
    long jourTotalEntreAchatDateEttFutur = Duration.between(this.t, tFutur).toDays();
    double anneesTotales = (double) jourTotalEntreAchatDateEttFutur / 365;
    double valeurFuture = this.valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, anneesTotales);
    return (int) valeurFuture;
  }
}
