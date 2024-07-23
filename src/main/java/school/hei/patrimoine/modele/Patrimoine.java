package school.hei.patrimoine.modele;

import java.util.List;
import java.util.stream.Stream;
import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

public record Patrimoine(
    String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
    implements Serializable/*note(no-serializable)*/ {
  public Patrimoine(String nom, Personne possesseur, LocalDate t, Set<Possession> possessions) {
    this.nom = nom;
    this.possesseur = possesseur;
    this.t = t;
    this.possessions = validerPossessions(possessions);
  }

  private Set<Possession> validerPossessions(Set<Possession> possessions) {
    List<Devise> distinct = possessions.stream().map(Possession::getDevise).distinct().toList();
    if (distinct.size() > 1 && distinct.contains(NON_NOMMEE)){
      throw new IllegalArgumentException("On ne peut mixer Devise.NON_NOMMEE avec autres devises");
    }
		return possessions;
  }

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public int getValeurComptable(Devise devise){
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

    public Patrimoine ajouterPossession(Possession nouvellePossession) {
        Set<Possession> nouvellesPossessions = new HashSet<>(possessions);
        nouvellesPossessions.add(nouvellePossession);
        return new Patrimoine(nom, possesseur, t, nouvellesPossessions);
    }

    public double getValeurComptableEnDevise(String devise, LocalDate dateEvaluation, double tauxChangeInitial, double tauxAppreciationAnnuelle) {

        double tauxChange = tauxChangeInitial * Math.pow(1 + tauxAppreciationAnnuelle, t.until(dateEvaluation).getDays());

        double valeurComptableEnEuro = getValeurComptable() * tauxChange;

        if ("Euro".equalsIgnoreCase(devise)) {
            return valeurComptableEnEuro;
        } else {

            throw new UnsupportedOperationException("Conversion vers d'autres devises n'est pas encore implémentée.");
        }
    }
}
