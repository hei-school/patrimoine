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
    long nombreDeJoursEntreTEtTFutur = ChronoUnit.DAYS.between(t, tFutur);
    double nombreDAnneesEntreTEtTFutur = nombreDeJoursEntreTEtTFutur / 365.25;
    double valeurComptableFutur = valeurComptable + valeurComptable * (tauxDAppreciationAnnuelle * nombreDAnneesEntreTEtTFutur);
    return new Materiel(nom, tFutur, (int) valeurComptableFutur, tauxDAppreciationAnnuelle);
  }
}
