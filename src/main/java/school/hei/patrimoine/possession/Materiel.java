package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.*;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  public Possession projectionFuture(Instant tFutur) {
    Duration difference = Duration.between(t, tFutur);
    long differenceEnJours = difference.toDays();
    double differenceEnAnnees = differenceEnJours / 365;

    double nouvelleValeurComptable = getValeurComptable()* Math.pow(tauxDAppreciationAnnuelle,differenceEnAnnees);

    return new Materiel(getNom(), tFutur, (int) nouvelleValeurComptable, tauxDAppreciationAnnuelle);
  }
}
