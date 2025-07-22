package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.vente.InformationDeVente;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.modele.vente.Vendable;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
    implements Serializable /*note(no-serializable)*/, Vendable
    permits AchatMaterielAuComptant,
        Compte,
        CompteCorrection,
        Correction,
        FluxArgent,
        GroupePossession,
        Materiel,
        PatrimoinePersonnel,
        PersonneMorale,
        RemboursementDette,
        TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  protected final InformationDeVente informationDeVente;
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.informationDeVente = new InformationDeVente();
  }

  public CompteCorrection getCompteCorrection() {
    if (compteCorrection == null) {
      compteCorrection = new CompteCorrection(nom, valeurComptable.devise());
    }
    return compteCorrection;
  }

  public Argent valeurComptable() {
    return valeurComptable;
  }

  public final Devise devise() {
    return valeurComptable.devise();
  }

  public final Argent valeurComptableFuture(LocalDate tFutur) {
    if (tFutur.isAfter(getDateDeVente())) {
      return valeurComptable.mult(0);
    }
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

  @Override
  public Set<Objectif> getObjectifs() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Set<ValeurMarche> getValeurMarches() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public ValeurMarche getValeurMarche(LocalDate t) {
    return informationDeVente.getValeurMarche(t);
  }

  @Override
  public void addValeurMarche(ValeurMarche v) {
    informationDeVente.addValeurMarche(v);
  }

  @Override
  public Argent getValeurDeVente() {
    return informationDeVente.getValeurDeVente();
  }

  @Override
  public LocalDate getDateDeVente() {
    return informationDeVente.getDateDeVente();
  }

  @Override
  public Compte getCompteBeneficiaire() {
    return informationDeVente.getCompteBeneficiaire();
  }

  @Override
  public boolean estVendue() {
    return informationDeVente.estVendue();
  }

  @Override
  public void vendre(Argent valeurDeVente, LocalDate dateDeVente, Compte compteBeneficiaire) {
    informationDeVente.confirmeVente(this, valeurDeVente, dateDeVente, compteBeneficiaire);
  }
}
