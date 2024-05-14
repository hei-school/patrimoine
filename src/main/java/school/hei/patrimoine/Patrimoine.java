package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(Personne possesseur , Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    int valeurComptable = 0;
    if (possessions.isEmpty()) {
      return 0;
    }
    else {
      for (Possession possession : possessions) {
        valeurComptable += possession.getValeurComptable();
      }
    }
    return valeurComptable;
  }

}
