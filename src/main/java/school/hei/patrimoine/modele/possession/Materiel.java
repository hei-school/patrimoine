package school.hei.patrimoine.modele.possession;

import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vente.InformationDeVente;

public final class Materiel extends Possession {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;

  public Materiel(
      String nom,
      LocalDate dateAcquisition,
      LocalDate t,
      Argent valeurComptable,
      double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  private Materiel(
      String nom,
      LocalDate dateAcquisition,
      LocalDate t,
      Argent valeurComptable,
      double tauxDAppreciationAnnuelle,
      InformationDeVente informationDeVente) {
    super(nom, t, valeurComptable, informationDeVente);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateAcquisition)) {
      return new Materiel(
          nom,
          dateAcquisition,
          tFutur,
          new Argent(0, valeurComptable.devise()),
          tauxDAppreciationAnnuelle,
          informationDeVente);
    }
    var joursEcoules = DAYS.between(t, tFutur);
    var valeurAjouteeJournaliere = valeurComptable.mult((tauxDAppreciationAnnuelle / 365.));
    var valeurFutureUnbound = valeurComptable.add(valeurAjouteeJournaliere.mult(joursEcoules), t);
    return new Materiel(
        nom,
        dateAcquisition,
        tFutur,
        valeurFutureUnbound.lt(0) ? new Argent(0, devise()) : valeurFutureUnbound,
        tauxDAppreciationAnnuelle,
        informationDeVente);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return IMMOBILISATION;
  }
}
