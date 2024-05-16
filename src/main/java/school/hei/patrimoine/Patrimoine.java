package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    int sommeValeursComptables = 0;

    for (Possession possession : possessions) {
      sommeValeursComptables += possession.getValeurComptable();
    }

    return sommeValeursComptables;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
