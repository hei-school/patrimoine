package school.hei.patrimoine.possession;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {

    Duration difference = Duration.between(t, tFutur);
    long nombreJours = difference.toDays();
    double valeurComptableFuture = valeurComptable;
    for (int i = 0; i < nombreJours; i++) {
      valeurComptableFuture *= (1 - tauxDAppreciationAnnuelle);
    }
    return (int)Math.round(valeurComptableFuture);
  }


}
