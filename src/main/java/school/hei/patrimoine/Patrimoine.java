package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    int sommePossessions = 0;
    for (Possession possessions : possessions) {
      sommePossessions += possessions.getValeurComptable();
    }
    return sommePossessions;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    Set<Possession> PossessionsFuture = new HashSet<>();
    for (Possession possession : possessions) {
      PossessionsFuture .add(possession.projectionFuture(tFutur));
    }
    return new Patrimoine(possesseur, tFutur, PossessionsFuture );
  }
}
