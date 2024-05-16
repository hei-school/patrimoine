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
    double nombresDeJoursEcouleesDepuisLAchat = TimeUnit.DAYS.toDays((tFutur.toEpochMilli()) - getT().toEpochMilli());
    double nombresDAnneesEcoulesDepuisLAchat = nombresDeJoursEcouleesDepuisLAchat / 365;

    double nouvelleValeurComptable = getValeurComptable()* Math.pow(tauxDAppreciationAnnuelle+1, nombresDAnneesEcoulesDepuisLAchat);

    return new Materiel(
            getNom(),
            tFutur,
            (int) nouvelleValeurComptable,
            tauxDAppreciationAnnuelle
    );
  }
}
