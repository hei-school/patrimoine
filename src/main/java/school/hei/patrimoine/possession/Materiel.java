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
    double interval = Duration.between(this.t, tFutur).toDays();
    double tauxParJour =  (valeurComptable * tauxDAppreciationAnnuelle) / 365;
    return valeurComptable + (int) tauxParJour * (int) interval;
  }

}
