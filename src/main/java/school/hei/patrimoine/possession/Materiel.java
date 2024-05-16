package school.hei.patrimoine.possession;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  private final static short JOURS_DANS_UNE_ANNEE = 365;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    double tauxParJour = valeurComptable * tauxDAppreciationAnnuelle / JOURS_DANS_UNE_ANNEE ;
    int tauxTotal = (int) Duration.between(t, tFutur).toDays() * (int) tauxParJour;
    int valeurComptableFutur = tauxTotal + valeurComptable;
    return new Materiel(nom, tFutur, valeurComptableFutur,tauxDAppreciationAnnuelle);
  }
}