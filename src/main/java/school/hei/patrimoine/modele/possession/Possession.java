package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectivable;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
    implements Serializable /*note(no-serializable)*/
        permits AchatMaterielAuComptant, Compte, CompteCorrection, Correction, Entreprise, FluxArgent, GroupePossession, Materiel, PatrimoinePersonnel, PersonneMorale, RemboursementDette, TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  // Note: valeurComptable is the value at time t, not the current value
  @EqualsAndHashCode.Exclude @ToString.Exclude private Argent valeurMarche;

  @EqualsAndHashCode.Exclude @ToString.Exclude private boolean estVendue = false;

  @EqualsAndHashCode.Exclude @ToString.Exclude private LocalDate dateVente;

  @EqualsAndHashCode.Exclude @ToString.Exclude private Compte compteBeneficiaire;

  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
  }

  public CompteCorrection getCompteCorrection() {
    if (compteCorrection == null) {
      compteCorrection = new CompteCorrection(nom, valeurComptable.devise());
    }
    return compteCorrection;
  }

  public Argent valeurComptable() {
    if (estVendue && dateVente != null && !t.isBefore(dateVente)) {
      return new Argent(0.0, valeurComptable.devise());
    }
    return valeurComptable;
  }

  public final Devise devise() {
    return valeurComptable.devise();
  }

  public final Argent valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).valeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public abstract TypeAgregat typeAgregat();

  @Override
  public String nom() {
    return nom;
  }

  @Override
  public Argent valeurAObjectifT(LocalDate t) {
    return projectionFuture(t).valeurComptable;
  }

  public Argent valeurMarche() {
    if (typeAgregat() == TypeAgregat.IMMOBILISATION
            || typeAgregat() == TypeAgregat.ENTREPRISE) {
      return valeurMarche != null ? valeurMarche : valeurComptable;
    }
    return valeurComptable;
  }

  public void vendre(LocalDate date, Argent montant, Compte compteDestination) {
    if (estVendue) {
      throw new IllegalStateException("Possession déjà vendue : " + this);
    }

    this.estVendue = true;
    this.dateVente = date;
    this.compteBeneficiaire = compteDestination;
    this.valeurMarche = montant;

    new TransfertArgent(
            "Vente de " + nom,
            null,
            compteDestination,
            date,
            montant
    );
  }
}
