package school.hei.patrimoine.possession;

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
    int nombreAnnee = (int) ((ChronoUnit.DAYS.between(t, tFutur) / 365));
    int valeurFuture = (int) (getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, nombreAnnee));

    return new Materiel(
            getNom(),
            tFutur,
            valeurFuture,
            tauxDAppreciationAnnuelle
    );
  }
}
