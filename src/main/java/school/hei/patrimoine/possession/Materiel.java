package school.hei.patrimoine.possession;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long joursEcoules = TimeUnit.MILLISECONDS.toDays(tFutur.toEpochMilli() - getT().toEpochMilli());
    double anneesEcoulees = joursEcoules / 365.0;

    double nouvelleValeurComptable = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, anneesEcoulees);

    return new Materiel(
            getNom(),
            tFutur,
            (int) nouvelleValeurComptable,
            tauxDAppreciationAnnuelle
    );
  }
}
