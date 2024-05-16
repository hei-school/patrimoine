package school.hei.patrimoine.possession;

import java.time.Duration;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long secondes = Duration.between(this.t, tFutur).getSeconds();
    double annees = secondes / (365.25 * 24 * 3600);
    double nouvelleValeur = this.valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, annees);
    return new Materiel(this.nom, tFutur, (int) nouvelleValeur, this.tauxDAppreciationAnnuelle);
  }
}
