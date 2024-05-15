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
  public int valeurComptableFuture(Instant tFutur){
    int valeurFutur=(int)(valeurComptable-(valeurComptable/((tFutur.getEpochSecond()-t.getEpochSecond())/31_536_000)));
    return valeurFutur;
  }
}
