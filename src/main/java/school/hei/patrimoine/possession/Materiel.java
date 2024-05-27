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
    double differenceDAnnee = ChronoUnit.DAYS.between(t, tFutur) / 365.2425;
    double valeurDeRecuperation = valeurComptable - Math.abs(tauxDAppreciationAnnuelle * differenceDAnnee);
    double baseAmortissable = valeurComptable - valeurDeRecuperation;
    double sommeDesChiffresDAnnee = (differenceDAnnee * (differenceDAnnee + 1)) / 2;
    double depreciationAccumule = baseAmortissable * (differenceDAnnee / sommeDesChiffresDAnnee);

    int valeurComptableFuture = (int) (valeurComptable - (valeurComptable * depreciationAccumule));
    double tauxDAppreciationFuture = tauxDAppreciationAnnuelle / sommeDesChiffresDAnnee;
    return new Materiel(nom, tFutur, valeurComptableFuture, tauxDAppreciationFuture);
  }
}