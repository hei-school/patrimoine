package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    int annee= ((tFutur.atZone(ZoneId.systemDefault()).getYear()) - t.atZone(ZoneId.systemDefault()).getYear());
    double valeurFuture = (getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, annee));

    return new Materiel(
            getNom(),
            tFutur,
            (int) valeurFuture,
            tauxDAppreciationAnnuelle
    );
  }
}