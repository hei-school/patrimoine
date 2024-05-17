package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    int possessionValeur = 0;
    for (Possession possession : possessions) {
      possessionValeur += possession.getValeurComptable();
    }
    return possessionValeur;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    return new Patrimoine(
            possesseur,
            tFutur,
            possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }
}
