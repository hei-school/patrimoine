package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
        String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
        implements Serializable {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  private LocalDate getDate() {
    return t; // Utilisation de la date t comme date de référence
  }

  public double getValeurEnDevise(String devise, double tauxChange, LocalDate dateEvaluation, double appreciationAnnee) {
    int valeurEnAr = getValeurComptable();
    long joursEntre = getDate().until(dateEvaluation).getDays();
    double tauxAppreciationQuotidien = Math.pow(1 + appreciationAnnee / 100, joursEntre / 365.0);

    double valeurConvertie = valeurEnAr / tauxChange * tauxAppreciationQuotidien;
    return valeurConvertie;
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
