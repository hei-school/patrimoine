package school.hei.patrimoine.modele;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;

public record Patrimoine(
    String nom, Set<Personne> possesseurs, LocalDate t, Set<Possession> possessions)
    implements Serializable /*note(no-serializable)*/ {
  public Patrimoine(
      String nom, Set<Personne> possesseurs, LocalDate t, Set<Possession> possessions) {
    this.nom = nom;
    this.possesseurs = possesseurs;
    this.t = t;
    this.possessions = validerPossessions(possessions);
  }

  public static Patrimoine of(
      String nom, Personne possesseur, LocalDate t, Set<Possession> possessions) {
    return new Patrimoine(nom, Set.of(possesseur), t, possessions);
  }

  private Set<Possession> validerPossessions(Set<Possession> possessions) {
    List<Devise> distinct = possessions.stream().map(Possession::getDevise).distinct().toList();
    if (distinct.size() > 1 && distinct.contains(NON_NOMMEE)) {
      throw new IllegalArgumentException("On ne peut mixer Devise.NON_NOMMEE avec autres devises");
    }
    return possessions;
  }

  public int getValeurComptable() {
    return possessions.stream().mapToInt(this::getValeurComptable).sum();
  }

  private int getValeurComptable(Possession possession) {
    var possesseursDePossession = possession.getPossesseurs();
    var possessionValeur = possession.getValeurComptable();
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

  public int getValeurComptable(Devise devise, LocalDate t) {
    return possessions.stream().mapToInt(p -> p.getValeurComptable(devise, t)).sum();
  }
}
