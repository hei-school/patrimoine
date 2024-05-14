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
    double tauxParJour = valeurComptable * tauxDAppreciationAnnuelle / 365 ;
    int tauxAcutel = (int) Duration.between(t, tFutur).toDays() * (int) tauxParJour;
    return valeurComptable + tauxAcutel;
  }
}
