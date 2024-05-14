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
    int jourDansLAnnee = 365;

    long jourEntreCreationDeLaPossessionEtTFutur = Duration.between(this.t, tFutur).toDays();
    long ansEntreCreationDeLaPossessionEtTFutur = jourEntreCreationDeLaPossessionEtTFutur / jourDansLAnnee;
    double valeurFinal = this.valeurComptable;
    for (int an = 0; an < ansEntreCreationDeLaPossessionEtTFutur; an++) {
      valeurFinal += valeurFinal * tauxDAppreciationAnnuelle;
    }

    return (int) valeurFinal;
  }
}
