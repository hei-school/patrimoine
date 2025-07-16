package school.hei.patrimoine.modele.possession;

import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.time.LocalDate;

import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class Materiel extends Possession {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;
  private Argent valeurMarche;

  public Materiel(
      String nom,
      LocalDate dateAcquisition,
      LocalDate t,
      Argent valeurComptable,
      Argent valeurMarche,
      double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    this.valeurMarche = (valeurMarche != null) ? valeurMarche : valeurComptable;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateAcquisition)) {
      return new Materiel(
          nom,
          dateAcquisition,
          tFutur,
          valeurComptable,
          new Argent(0, valeurComptable.devise()),
          tauxDAppreciationAnnuelle);
    }
    var joursEcoules = DAYS.between(t, tFutur);
    var valeurAjouteeJournaliere = valeurComptable.mult((tauxDAppreciationAnnuelle / 365.));
    var valeurFutureUnbound = valeurComptable.add(valeurAjouteeJournaliere.mult(joursEcoules), t);
    var nouvelleValeurMarche = valeurMarche.add(valeurAjouteeJournaliere.mult(joursEcoules), tFutur);

    return new Materiel(
        nom,
        dateAcquisition,
        tFutur,
        valeurFutureUnbound.lt(0) ? new Argent(0, devise()) : valeurFutureUnbound,
        nouvelleValeurMarche.lt(0) ? new Argent(0, devise()) : nouvelleValeurMarche,
        tauxDAppreciationAnnuelle);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return IMMOBILISATION;
  }
}
