package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.TreeMap;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectivable;

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
        TransfertArgent,
        Entreprise {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  private final NavigableMap<LocalDate, Argent> historiqueValeurMarche = new TreeMap<>();
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.historiqueValeurMarche.put(t, valeurComptable);
  }

  public Possession(String nom, LocalDate t, Argent valeurComptable,Argent valeurMarche) {
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

  public void enregistrerValeurMarche(LocalDate date, Argent valeur) {
    //check type
    historiqueValeurMarche.put(date, valeur);
  }

  public Argent valeurMarcheALaDate(LocalDate date) {
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
}
