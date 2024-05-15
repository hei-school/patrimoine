package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
      double tauxDAppreciationAnnuelle = 0.1;
      int futur_valeur = (int) (valeurComptable * tauxDAppreciationAnnuelle);
      return futur_valeur;
     }

  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();

  }
}
