package school.hei.patrimoine.modele;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Possession;

@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@Getter
public class Patrimoine implements Serializable, Objectivable /*note(no-serializable)*/ {

  private final String nom;
  private final Devise devise;
  private final Set<Personne> possesseurs;
  private final LocalDate t;
  private final Set<Possession> possessions;

  public static Patrimoine of(
      String nom,
      Devise devise,
      Set<Personne> possesseurs,
      LocalDate t,
      Set<Possession> possessions) {
    return new Patrimoine(nom, devise, possesseurs, t, withComptesCorrections(possessions));
  }

  public static Patrimoine of(
      String nom, Devise devise, Personne possesseur, LocalDate t, Set<Possession> possessions) {
    return new Patrimoine(nom, devise, Set.of(possesseur), t, withComptesCorrections(possessions));
  }

  private static Set<Possession> withComptesCorrections(Set<Possession> possessions) {
    var set = new HashSet<Possession>();
    possessions.forEach(
        p -> {
          set.add(p);
          set.add(p.getCompteCorrection());
        });
    return set;
  }

  public Argent getValeurComptable() {
    return getValeurComptable(devise);
  }

  public Argent getValeurComptable(Devise devise) {
    return new Argent(
        possessions.stream()
            .filter(not(p -> p instanceof CompteCorrection))
            .mapToInt(p -> valeurPartagéeEntrePossesseursDePatrimoine(p, devise))
            .sum(),
        devise);
  }

  private int valeurPartagéeEntrePossesseursDePatrimoine(Possession possession, Devise cible) {
    var possesseursDePossession = possession.getPossesseurs();
    int possessionValeur = possession.valeurComptable().convertir(cible, t).montant();
    if (possesseursDePossession.isEmpty()) {
      return possessionValeur;
    }

    return possesseurs.stream()
        .mapToInt(
            possesseurDePatrimoine ->
                (int)
                    (possessionValeur
                        * possesseursDePossession.getOrDefault(possesseurDePatrimoine, 0.)))
        .sum();
  }

  public Patrimoine projectionFuture(LocalDate tFutur) {
    return new Patrimoine(
        nom,
        devise,
        possesseurs,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }

  public Possession possessionParNom(String nom) {
    return possessions.stream().filter(p -> nom.equals(p.getNom())).findFirst().orElseThrow();
  }

  @Override
  public String nom() {
    return nom;
  }

  @Override
  public Argent valeurAObjectifT(LocalDate t) {
    return projectionFuture(t).getValeurComptable();
  }
}
