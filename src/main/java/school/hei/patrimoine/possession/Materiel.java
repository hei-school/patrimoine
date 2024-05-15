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
  public int valeurComptableFuture(Instant tFutur) {
  double nombreDeJours = ChronoUnit.DAYS.between(t, tFutur);
  double tauxDAppreciationParJour = tauxDAppreciationAnnuelle / nombreDeJours;
  double valeurComptableFuture = valeurComptable * tauxDAppreciationParJour;

  return (int)valeurComptableFuture;
  }
}
