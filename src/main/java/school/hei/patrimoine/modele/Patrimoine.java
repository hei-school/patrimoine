package school.hei.patrimoine.modele;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

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
  private final Set<Personne> possesseurs;
  private final LocalDate t;
  private final Set<Possession> possessions;

  public static Patrimoine of(
      String nom, Personne possesseur, LocalDate t, Set<Possession> possessions) {
    return Patrimoine.of(nom, Set.of(possesseur), t, possessions);
  }

  public static Patrimoine of(
      String nom, Set<Personne> possesseurs, LocalDate t, Set<Possession> possessions) {
    return new Patrimoine(
        nom, possesseurs, t, withComptesCorrections(validerPossessions(possessions)));
  }

  private static Set<Possession> validerPossessions(Set<Possession> possessions) {
    var devises = possessions.stream().map(Possession::getDevise).distinct().toList();
    if (devises.size() > 1 && devises.contains(NON_NOMMEE)) {
      throw new IllegalArgumentException("On ne peut mixer Devise.NON_NOMMEE avec autres devises");
    }
    return possessions;
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

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    return getValeurComptable(possessions.toArray(new Possession[0])[0].getDevise());
  }

  public int getValeurComptable(Devise devise) {
    return possessions.stream()
        .filter(not(p -> p instanceof CompteCorrection))
        .mapToInt(p -> valeurPartagéeEntrePossesseursDePatrimoine(p, devise))
        .sum();
  }

  private int valeurPartagéeEntrePossesseursDePatrimoine(Possession possession, Devise devise) {
    var possesseursDePossession = possession.getPossesseurs();
    var possessionValeur = possession.valeurComptable(devise);
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
  public int valeurAObjectifT(LocalDate t) {
    return projectionFuture(t).getValeurComptable();
  }
}
