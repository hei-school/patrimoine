package school.hei.patrimoine.modele.possession;

import static java.lang.Math.max;
import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public final class Materiel extends Possession {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;

  public Materiel(
      String nom,
      LocalDate t,
      Argent valeurComptable,
      LocalDate dateAcquisition,
      double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateAcquisition)) {
      return new Materiel(
          nom,
          tFutur,
          new Argent(0, valeurComptable.devise()),
          dateAcquisition,
          tauxDAppreciationAnnuelle);
    }
    var joursEcoules = DAYS.between(t, tFutur);
    double valeurAjouteeJournaliere =
        valeurComptable.montant() * (tauxDAppreciationAnnuelle / 365.);
    int valeurComptableFuture =
        max(0, (int) (valeurComptable.montant() + valeurAjouteeJournaliere * joursEcoules));
    return new Materiel(
        nom,
        tFutur,
        new Argent(valeurComptableFuture, valeurComptable.devise()),
        dateAcquisition,
        tauxDAppreciationAnnuelle);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return IMMOBILISATION;
  }
}
