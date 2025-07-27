package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.TreeMap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.vente.Vendable;

import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;
import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;


@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
        implements Serializable, Vendable/*note(no-serializable)*/
        permits
        AchatMaterielAuComptant,
        Compte,
        CompteCorrection,
        Correction,
        FluxArgent,
        GroupePossession,
        PatrimoinePersonnel,
        PersonneMorale,
        RemboursementDette,
        Entreprise,
        Materiel,
        TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  protected final NavigableMap<LocalDate, Argent> historiqueValeurMarche = new TreeMap<>();
  protected  LocalDate dateVente;
  protected  Argent prixVente;
  protected  Compte compteBeneficiaire;
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;


  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.historiqueValeurMarche.put(t, valeurComptable);
  }

  public Possession(String nom, LocalDate t, Argent valeurComptable, Argent valeurMarche, LocalDate dateVente, Argent prixVente, Compte compteBeneficiaire) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.historiqueValeurMarche.put(t, valeurMarche);
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

  public Argent valeurMarche() {
    return valeurMarcheALaDate(t);
  }

  public void addValeurMarche(LocalDate date, Argent valeur) {
    if (typeAgregat() != IMMOBILISATION && typeAgregat() != ENTREPRISE) {
      throw new UnsupportedOperationException(
              "Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de marché."
      );
    }
    historiqueValeurMarche.put(date, valeur);
  }

  public Argent valeurMarcheALaDate(LocalDate date) {
    if (estVendue(date)) {
      return new Argent(0, valeurComptable.devise());
    }
    LocalDate key = historiqueValeurMarche.floorKey(date);
    if (key != null) {
      return historiqueValeurMarche.get(key);
    }
    return valeurComptable();
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

  public Argent valeurComptableEffective(LocalDate date) {
    if (dateVente != null && (date.isEqual(dateVente) || date.isAfter(dateVente))) {
      return new Argent(0, valeurComptable.devise());
    }
    return valeurComptable;
  }

  public Argent valeurComptableEffective() {
    return valeurComptableEffective(LocalDate.now());
  }

  @Override
  public void vendre(LocalDate date, Argent prix, Compte compteBeneficiaire) {
    if (this.dateVente != null) {
      throw new IllegalStateException("Déjà vendue");
    }
    this.dateVente = date;
    this.prixVente = prix;
    this.compteBeneficiaire = compteBeneficiaire;
    compteBeneficiaire.ajouter(prix);
  }

  @Override
  public boolean estVendue() {
    return dateVente != null;
  }

  @Override
  public boolean estVendue(LocalDate date) {
    return dateVente != null && !date.isBefore(dateVente);
  }

  @Override
  public LocalDate getDateVente() {
    return dateVente;
  }

  @Override
  public Argent getPrixVente() {
    return prixVente;
  }

}