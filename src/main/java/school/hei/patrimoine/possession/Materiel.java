package school.hei.patrimoine.possession;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, LocalDate t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    var joursEcoules = DAYS.between(t, tFutur);
    double valeurAjouteeJournaliere = valeurComptable * (tauxDAppreciationAnnuelle / 365.);
    int valeurComptableFuture = (int) (valeurComptable + valeurAjouteeJournaliere * joursEcoules);
    return new Materiel(nom, tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
