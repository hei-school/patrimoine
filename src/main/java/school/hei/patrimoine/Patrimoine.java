package school.hei.patrimoine;

import static java.util.stream.Collectors.toSet;

import java.time.Instant;
import java.util.Set;
import school.hei.patrimoine.possession.Possession;

public record Patrimoine(Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    int somme = 0;
    for (Possession possession : possessions) {
      somme += possession.getValeurComptable();
    }
    return somme;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    return new Patrimoine(
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }
}
