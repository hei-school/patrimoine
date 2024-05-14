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
    Duration duree = Duration.between(t, tFutur);
    double differenceEnAnnees = duree.toDays() / 365.2425;

    double valeurDeRecuperation = valeurComptable - Math.abs(tauxDAppreciationAnnuelle * differenceEnAnnees);
    double baseAmortissable = valeurComptable - valeurDeRecuperation;
    double sommeDesChiffresDAnnee = (differenceEnAnnees * (differenceEnAnnees + 1)) / 2;
    double depreciationAccumulee = baseAmortissable * (differenceEnAnnees / sommeDesChiffresDAnnee);
    double valeurComptableFuture = valeurComptable - (valeurComptable * depreciationAccumulee);

    return (int) valeurComptableFuture;
  }
}
