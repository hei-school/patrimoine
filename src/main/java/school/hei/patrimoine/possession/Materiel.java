package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  private static final int JOURS_DANS_UN_ANS = 365;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    long anneeEntreTetTFutur = Duration.between(t, tFutur).toDays() / JOURS_DANS_UN_ANS;
    return (int) (tauxDAppreciationAnnuelle * valeurComptable * anneeEntreTetTFutur) + valeurComptable;
  }
}
