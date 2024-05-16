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
    double nombreAnnees = Duration.between(t, tFutur).toDays() / 365;
    double valeurComptableFuture = getValeurComptable() * Math.pow((1 + tauxDAppreciationAnnuelle), nombreAnnees);
    return new Materiel(this.getNom(), tFutur, (int) valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
