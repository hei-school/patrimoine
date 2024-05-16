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
  public Possession projectionFuture(Instant tFutur) {
    long joursPassés = (tFutur.toEpochMilli() - getT().toEpochMilli()) / (24 * 60 * 60 * 1000);
    double annéesPassés = joursPassés / 365.0;
    double nouvelleValeurComptable = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, annéesPassés);

    return new Materiel(
            getNom(),
            tFutur,
            (int) nouvelleValeurComptable,
            tauxDAppreciationAnnuelle);
  }
}
