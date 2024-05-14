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
    double jours = (double) (Duration.between(this.t, tFutur).toDays() / 365);
    int annees = (int) Math.round(jours);
    int valeur = (int)(this.valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, annees));
    return valeur;
  }
}
