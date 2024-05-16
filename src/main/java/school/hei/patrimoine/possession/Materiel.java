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
    long millisecondsParAnnee = 1000L * 60 * 60 * 24 * 365;

    long anneesEcoulee = Math.round((tFutur.toEpochMilli() - t.toEpochMilli()) / (double) millisecondsParAnnee);
    int valeurComptableDepreciee = valeurComptable;
    for (int i = 0; i < anneesEcoulee; i++) {
      valeurComptableDepreciee -= (int) (valeurComptableDepreciee * -tauxDAppreciationAnnuelle);
      System.out.println(valeurComptableDepreciee);
    }

    return new Materiel(nom,t,valeurComptableDepreciee,tauxDAppreciationAnnuelle);
  }
}
