package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

import static java.lang.Math.max;
public final class Materiel extends Possession {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;

  public Materiel(
      String nom, LocalDate t, int valeurComptable, LocalDate dateAcquisition, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateAcquisition)) {
      return new Materiel(nom, tFutur, 0, dateAcquisition, tauxDAppreciationAnnuelle);
    }

    // Calculer les jours écoulés sans utiliser ChronoUnit.DAYS
    long joursEcoules = calculateDaysBetween(t, tFutur);

    double valeurAjouteeJournaliere = valeurComptable * (tauxDAppreciationAnnuelle / 365.0);
    int valeurComptableFuture = max(0, (int) (valeurComptable + valeurAjouteeJournaliere * joursEcoules));
    return new Materiel(nom, tFutur, valeurComptableFuture, dateAcquisition, tauxDAppreciationAnnuelle);
  }

  private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
    return endDate.toEpochDay() - startDate.toEpochDay();
  }
}
