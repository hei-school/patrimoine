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
    double tauxParJour = valeurComptable * tauxDAppreciationAnnuelle / 365 ;
    int tauxTotal = (int) Duration.between(t, tFutur).toDays() * (int) tauxParJour;
    int valeurComptableFutur = tauxTotal + valeurComptable;
    return new Materiel(nom, tFutur, valeurComptableFutur,tauxDAppreciationAnnuelle);
  }
}
