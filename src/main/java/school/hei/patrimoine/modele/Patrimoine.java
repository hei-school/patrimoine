package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
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
  public double convertToEuro(LocalDate date, Map<String, Double> exchangeRates, double annualAppreciationRate) {
    double valueInLocalCurrency = 0;
    for (Possession possession : possessions) {
      if (possession instanceof Argent) {
        Argent argent = (Argent) possession;
        double localValue = argent.getValeurComptable();
        double euroEquivalent = localValue / exchangeRates.getOrDefault(argent.getValeurComptable(), 1.0);
        valueInLocalCurrency += euroEquivalent;
      }
    }
    // Appliquer le taux d'appréciation annuelle
    double appreciationFactor = Math.pow((1 + annualAppreciationRate / 100), date.getYear() - 2024);
    return valueInLocalCurrency * appreciationFactor;
  }
}
