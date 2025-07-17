package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.ValeurMarche;
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
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;
  private final Set<ValeurMarche> valeursMarche = new HashSet<>();

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
              .filter(vm -> !vm.date().isAfter(date))
              .max(Comparator.comparing(ValeurMarche::date))
              .map(ValeurMarche::valeur)
              .orElse(valeurComptable);
    }
    return valeurComptable;
  }

  public Set<ValeurMarche> historiqueValeurMarche() {
    return new HashSet<>(valeursMarche);
  }
}
