package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    int totalValeurComptable = 0;
    if (possessions.isEmpty()) {
      return totalValeurComptable;
    }
    for (Possession possession : possessions){
      totalValeurComptable +=possession.getValeurComptable();
    }
    throw new NotImplemented();
  }
}
