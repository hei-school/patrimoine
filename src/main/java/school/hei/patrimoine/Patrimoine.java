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
    int valeurComptableTotale = 0;
    for (Possession possession : possessions) {
      valeurComptableTotale += possession.getValeurComptable();
    }
    return valeurComptableTotale;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    Set<Possession> futurePossessions = new HashSet<>();
    for (Possession possession : possessions) {
      futurePossessions.add(possession.projectionFuture(tFutur));
    }
    return new Patrimoine(possesseur, tFutur, futurePossessions);
  }
}
