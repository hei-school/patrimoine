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

    double valeurCompAnuelle = valeurComptable * tauxDAppreciationAnnuelle;
    double nbAnnees = ChronoUnit.DAYS.between(t, tFutur) / 365.2425;
    int valeurComptableFuture = (int) Math.max(valeurComptable - (valeurCompAnuelle * nbAnnees), 0);

    Materiel materiel = new Materiel(
            nom, tFutur, valeurComptableFuture, this.tauxDAppreciationAnnuelle
    );

    return materiel;

  }
}
