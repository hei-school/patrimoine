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
  public Possession projectionFuture(Instant tFutur) {
    double tauxDAppreciationJournalier = ChronoUnit.DAYS.between(getT(), tFutur) / 365.0;
    double nouvelleValeurComptable = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, tauxDAppreciationJournalier);

    return new Materiel(getNom(), tFutur, (int) nouvelleValeurComptable, tauxDAppreciationAnnuelle);
  }
}
