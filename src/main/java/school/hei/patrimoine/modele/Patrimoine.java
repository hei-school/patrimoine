package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
    String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
    implements Serializable/*note(no-serializable)*/ {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
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
