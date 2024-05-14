package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    if (possessions.isEmpty()){
      return 0;
    }
    return this.possessions.stream().map(Possession::getValeurComptable).reduce(0, Integer::sum);
  }
}

