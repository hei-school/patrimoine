package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.vente.ValeurMarche;
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
        TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  protected final Set<ValeurMarche> valeursMarche;
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  public Possession(String nom, LocalDate t, Argent valeurComptable, Set<ValeurMarche> valeursMarche) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.valeursMarche = valeursMarche;
  }

  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    valeursMarche = new HashSet<>(Set.of(new ValeurMarche(t,  valeurComptable)));
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

  public Argent valeurMarche() {
    return valeurMarche(LocalDate.now());
  }

  public Argent valeurMarche(LocalDate date) {
    if (typeAgregat() == TypeAgregat.IMMOBILISATION || typeAgregat() == TypeAgregat.ENTREPRISE) {
      return valeursMarche.stream()
              .filter(vm -> !vm.t().isAfter(date))
              .max(Comparator.comparing(ValeurMarche::t))
              .map(ValeurMarche::valeurComptable)
              .orElse(valeurComptable);
    }
    return valeurComptable;
  }

  public Set<ValeurMarche> historiqueValeurMarche() {
    return new HashSet<>(valeursMarche);
  }
}
