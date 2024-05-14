package school.hei.patrimoine.possession;

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
    long differenceJours = ChronoUnit.DAYS.between(t, tFutur);
    double tauxDAppreciationJournalier = tauxDAppreciationAnnuelle / 365.0;
    double valeurComptableFuture = valeurComptable * Math.pow(1 + tauxDAppreciationJournalier, differenceJours);
    return (int) Math.round(valeurComptableFuture);
  }
}