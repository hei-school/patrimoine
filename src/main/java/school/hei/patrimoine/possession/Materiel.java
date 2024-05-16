package school.hei.patrimoine.possession;

import java.time.temporal.ChronoUnit;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
 int diffDay = (int) ChronoUnit.DAYS.between(t, tFutur);
    int tauxByDay = (int) Math.abs((tauxDAppreciationAnnuelle * valeurComptable /100) /365.5);
    int newValeurComptable = valeurComptable - (diffDay * tauxByDay);
    return new Materiel(this.nom, tFutur, newValeurComptable, tauxDAppreciationAnnuelle);
  }
  
}