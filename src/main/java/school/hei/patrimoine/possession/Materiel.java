package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.Duration;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override

  public int valeurComptableFuture(Instant tFutur) {
    long  differenceDAnnees = Duration.between(t, tFutur).toDays() /365;
    double valeurFuture = valeurComptable + ((valeurComptable * tauxDAppreciationAnnuelle) * differenceDAnnees);
    return (int) valeurFuture;
  
  
  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
}