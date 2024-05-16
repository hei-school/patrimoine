package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isBefore(getT())) {
      throw new IllegalArgumentException("La date future ne peut être antérieure à la date actuelle.");
    }
    long joursEcoule = ChronoUnit.DAYS.between(getT(), tFutur);
    double tauxAnnuel = tauxDAppreciationAnnuelle / 365;
    double appreciation = Math.pow(1 + tauxAnnuel, joursEcoule);
    int ValeurRecente = (int) (getValeurComptable() * appreciation);
    return new Materiel(getNom(), tFutur, ValeurRecente, tauxDAppreciationAnnuelle);
  }

}
