package school.hei.patrimoine.modele.currency;

import lombok.Getter;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
public class TauxDeChange {
  private final double valeur;
  private final Devise deviseSource;
  private final Devise deviseDestination;
  private final double tauxAppreciationAnnuel;
  private final LocalDate t;

  public TauxDeChange(
      Devise deviseSource,
      Devise deviseDestination,
      double valeur,
      double tauxAppreciationAnnuel,
      LocalDate t) {
    this.valeur = valeur;
    this.deviseSource = deviseSource;
    this.deviseDestination = deviseDestination;
    this.tauxAppreciationAnnuel = tauxAppreciationAnnuel;
    this.t = t;

    deviseSource.addTauxDeChange(this);
    deviseDestination.addTauxDeChange(this);
  }

  public TauxDeChange projectionFuture(LocalDate tFutur) {
    var joursEcoules = DAYS.between(t, tFutur);
    double valeurAjouteeJournalière = valeur * (tauxAppreciationAnnuel / 365.);
    double nouvelleValeur = valeur + (valeurAjouteeJournalière * joursEcoules);
    return new TauxDeChange(
        deviseSource, deviseDestination, nouvelleValeur, tauxAppreciationAnnuel, tFutur);
  }
}
