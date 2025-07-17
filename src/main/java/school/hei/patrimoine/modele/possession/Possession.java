package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.modele.vente.Vendable;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
        implements Serializable /*note(no-serializable)*/
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
  protected final Set<ValeurMarche> valeurMarches;

  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  public Possession(String nom, LocalDate t, Argent valeurComptable, Set<ValeurMarche> valeurMarche) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.valeurMarches = valeurMarche;
  }

  protected Possession(String nom, LocalDate t, Argent valeurComptable) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    valeurMarches = Set.of();
  }

  public Void vendre(LocalDate dateVente, Compte compteBenificiaire) {
    //this.dateVente = dateVente;
    throw new RuntimeException("Vente de possession non implémentée : " + this.getClass().getSimpleName());
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
}