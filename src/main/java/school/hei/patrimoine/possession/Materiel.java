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
    int nombreDeJoursDansUneAnnée = 365;
    int nombreDeJoursDepuisAcquisition = (int) ChronoUnit.DAYS.between(t, tFutur);
    int valeurComptableFuture = valeurComptable;
    while (nombreDeJoursDepuisAcquisition >= nombreDeJoursDansUneAnnée) {
      valeurComptableFuture = (int) (valeurComptableFuture - (valeurComptableFuture * tauxDAppreciationAnnuelle));
      nombreDeJoursDepuisAcquisition -= nombreDeJoursDansUneAnnée;
    }

    if (nombreDeJoursDepuisAcquisition != 0) {
      valeurComptableFuture = (int) (valeurComptableFuture - (valeurComptableFuture * (tauxDAppreciationAnnuelle / (nombreDeJoursDansUneAnnée / nombreDeJoursDepuisAcquisition))));
    }

    return new Materiel(nom, tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
