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
    double dateProjeteeEnAnnee = (ChronoUnit.DAYS.between(t, tFutur)) / 365.0;
    double valeurAjouteeDuMateriel = tauxDAppreciationAnnuelle * dateProjeteeEnAnnee;
    int valeurComptableProjetee = (int) (valeurComptable + valeurAjouteeDuMateriel);
    return new Materiel(nom, tFutur, valeurComptableProjetee, tauxDAppreciationAnnuelle);
  }
}
