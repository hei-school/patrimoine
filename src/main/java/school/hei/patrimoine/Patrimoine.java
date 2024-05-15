package school.hei.patrimoine;

import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }else {
      int totalValeurComptable = 0;
      for (Possession possession : possessions){
        totalValeurComptable += possession.getValeurComptable();
      }
      return totalValeurComptable;
    }
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
