package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    int totalValue = 0;
    for (Possession possesion : possessions) {
      totalValue += possesion.getValeurComptable();
    }
    return totalValue;
  }
}
