package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int getValeurComptable() {
    return super.getValeurComptable();
  }


  @Override
  public int valeurComptableFuture(Instant tFutur) {
    long joursEcoule = ChronoUnit.DAYS.between(t, tFutur);
    double anneesEcoulee = joursEcoule / 365.25;
    double tauxDepreciationTotal = Math.pow(1 + tauxDAppreciationAnnuelle, anneesEcoulee);
    return (int) (valeurComptable * tauxDepreciationTotal);
  }
}
