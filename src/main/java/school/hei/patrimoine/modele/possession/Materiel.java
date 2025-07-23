package school.hei.patrimoine.modele.possession;

import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.time.LocalDate;

import lombok.Getter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vendre.Vendable;

@Getter
public final class Materiel extends Possession implements Vendable {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;
  protected LocalDate dateVente;
  protected Argent prixVente;

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

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (estVendue() && !tFutur.isBefore(dateVente)) {
      return new Materiel(
              nom,
              dateAcquisition,
              tFutur,
              new Argent(0, devise()),
              tauxDAppreciationAnnuelle);
    }


    if (tFutur.isBefore(dateAcquisition)) {
      return new Materiel(
          nom,
          dateAcquisition,
          tFutur,
          new Argent(0, valeurComptable.devise()),
          tauxDAppreciationAnnuelle);
    }
    var joursEcoules = DAYS.between(t, tFutur);
    var valeurAjouteeJournaliere = valeurComptable.mult((tauxDAppreciationAnnuelle / 365.));
    var valeurFutureUnbound = valeurComptable.add(valeurAjouteeJournaliere.mult(joursEcoules), t);

    return new Materiel(
        nom,
        dateAcquisition,
        tFutur,
        valeurFutureUnbound.lt(0) ? new Argent(0, devise()) : valeurFutureUnbound,
        tauxDAppreciationAnnuelle);
  }

  @Override
  public void vendre(LocalDate date, Argent prix, Compte compteBeneficiaire) {
    this.dateVente = date;
    this.prixVente = prix;

    // Correction comptable : valeur devient 0
    new FluxArgent("Correction valeur comptable de " + nom,
            getCompteCorrection().getCompte(),
            date, valeurComptable.negate());

    // Flux d'entrée pour le bénéficiaire
    new FluxArgent("Vente de " + nom, compteBeneficiaire, date, prix);
  }

  @Override
  public boolean estVendue() {
    return dateVente != null;
  }

  @Override
  public boolean estActiveALaDate(LocalDate date) {
    return dateVente == null || date.isBefore(dateVente);
  }

  @Override
  public LocalDate getDateVente() {
    return dateVente;
  }

  @Override
  public Argent getPrixVente() {
    return prixVente;
  }


  @Override
  public TypeAgregat typeAgregat() {
    return IMMOBILISATION;
  }

}
