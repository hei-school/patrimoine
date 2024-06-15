package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions)
    implements Serializable/*note(no-serializable)*/ {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    return new Patrimoine(
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }
}
