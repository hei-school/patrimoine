package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession { private final double tauxAppreciationAnnuel;

  public Materiel(String nom, Instant dateAcquisition, int valeurComptable, double tauxAppreciationAnnuel) {
    super(nom, dateAcquisition, valeurComptable);
    this.tauxAppreciationAnnuel = tauxAppreciationAnnuel;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    long diffrence_de_temps = t.until(tFutur, ChronoUnit.DAYS);
    double tauxAnnuel = 1 + tauxAppreciationAnnuel;

    int valeurComptableFuture = valeurComptable;
    for (int i = 0; i < diffrence_de_temps; i++) {
      valeurComptableFuture *= tauxAnnuel;
    }

    return valeurComptableFuture;
  }


}
