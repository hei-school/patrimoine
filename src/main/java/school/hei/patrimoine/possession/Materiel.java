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
  public int valeurComptableFuture(Instant tFutur)  {
    long years = tFutur.getEpochSecond() - this.getT().getEpochSecond();
    double appreciationFactor = Math.pow(1 + tauxDAppreciationAnnuelle, years / (60 * 60 * 24 * 365.25));
    return (int) (this.getValeurComptable() * appreciationFactor);
  }
}
