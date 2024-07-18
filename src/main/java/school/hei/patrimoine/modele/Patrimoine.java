package school.hei.patrimoine.modele;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;

public record Patrimoine(String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
    implements Serializable /*note(no-serializable)*/ {
  public Patrimoine(String nom, Personne possesseur, LocalDate t, Set<Possession> possessions) {
    this.nom = nom;
    this.possesseur = possesseur;
    this.t = t;
    this.possessions = validerPossessions(possessions);
  }

  private Set<Possession> validerPossessions(Set<Possession> possessions) {
    List<Devise> distinct = possessions.stream().map(Possession::getDevise).distinct().toList();
    if (distinct.size() > 1 && distinct.contains(NON_NOMMEE)) {
      throw new IllegalArgumentException("On ne peut mixer Devise.NON_NOMMEE avec autres devises");
    }
    return possessions;
  }

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public int getValeurComptable(Devise devise) {
    return possessions.stream().mapToInt(p -> p.getValeurComptable(devise, t)).sum();
  }

  public Patrimoine projectionFuture(LocalDate tFutur) {
    return new Patrimoine(
        nom,
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }

  public Possession possessionParNom(String nom) {
    return possessions.stream().filter(p -> nom.equals(p.getNom())).findFirst().orElseThrow();
  }
}
