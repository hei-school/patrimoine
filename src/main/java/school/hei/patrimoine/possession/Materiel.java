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
  public Possession projectionFuture(Instant tFutur) {
    long duree = Duration.between(getT(), tFutur).toDays();
    int nombreDAnnees = (int) (duree / 365);
    double valeurComptableFuture = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, nombreDAnnees);
    return new Materiel(getNom(), tFutur, (int) valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
