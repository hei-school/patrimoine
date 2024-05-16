package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    int valeurComptable = 0;
    if (possessions.isEmpty()) {
      return valeurComptable;
    } else {
      for(Possession possession : possessions){
        valeurComptable += possession.getValeurComptable();
      }
    }
    return valeurComptable;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    Set<Possession> futurePossessions = possessions.stream()
            .map(possession -> possession.projectionFuture(tFutur))
            .collect(Collectors.toSet());
    return new Patrimoine(possesseur, tFutur, futurePossessions);
  }
}
