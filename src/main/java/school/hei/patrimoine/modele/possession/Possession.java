package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.calculation.ValeurCalculation;
import school.hei.patrimoine.modele.calculation.ValeurMarcheCase;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.vente.ValeurMarche;
import school.hei.patrimoine.modele.vente.Vendable;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
    implements Vendable, Serializable /*note(no-serializable)*/
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
  @EqualsAndHashCode.Exclude @ToString.Exclude private boolean estVendu = false;
  @EqualsAndHashCode.Exclude @ToString.Exclude private LocalDate dateVente;
  @EqualsAndHashCode.Exclude @ToString.Exclude private Argent prixVente;

  protected Possession(String nom, LocalDate t, Argent valeurComptable) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.valeurMarches = new HashSet<>();
    this.valeurMarches.add(new ValeurMarche(t, valeurComptable));
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

  @Override
  public void vendre(LocalDate dateVente, Argent prixVente, Compte compteBeneficiaire) {
    if (estVendu) throw new IllegalStateException("La possession a déjà été vendue.");
    if (compteBeneficiaire == null)
      throw new IllegalArgumentException("Le compte bénéficiaire ne peut pas être null.");
    if (dateVente == null)
      throw new IllegalArgumentException("La date de vente ne peut pas être null.");
    if (prixVente == null)
      throw new IllegalArgumentException("Le prix de vente doit être positif.");

    this.estVendu = true;
    this.dateVente = dateVente;
    this.prixVente = prixVente;

    Compte source = new Compte("Vente de " + nom, dateVente, this.valeurComptable);

    new TransfertArgent("Vente de " + nom, source, compteBeneficiaire, dateVente, prixVente);
  }

  @Override
  public boolean estVendu() {
    return estVendu;
  }

  @Override
  public Optional<LocalDate> getDateVente() {
    return Optional.ofNullable(dateVente);
  }

  @Override
  public Optional<Argent> getPrixVente() {
    return Optional.ofNullable(prixVente);
  }

  public void ajouterValeurMarche(ValeurMarche valeurMarche) {
    if (typeAgregat() != TypeAgregat.IMMOBILISATION && typeAgregat() != TypeAgregat.ENTREPRISE) {
      throw new UnsupportedOperationException(
          "Seules les IMMOBILISATIONs et ENTREPRISEs peuvent avoir une valeur de marché");
    }
    valeurMarches.add(valeurMarche);
  }


  public Argent getValeurMarche(LocalDate date) {
    // depends on type for the choice of calculation mode with  ValeurMarcheCase
    ValeurMarcheCase calculator = ValeurCalculation.getCalculation(this.typeAgregat());
    return calculator.calculateValeurCase(this, date);
  }
  public Set<ValeurMarche> historiqueValeurMarche() {
    return new HashSet<>(valeurMarches);
  }
}
