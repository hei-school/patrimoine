package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    throw new NotImplemented();
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    Set<Possession> futurPossessions = possessions
            .stream().map(possession -> possession.projectionFuture(t))
            .collect(Collectors.toSet());
    return  new Patrimoine(possesseur, tFutur, futurPossessions);
  }
}