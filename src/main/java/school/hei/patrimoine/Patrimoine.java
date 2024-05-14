package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    int sommVComptable=0;
    if (possessions.isEmpty()) {
      return 0;
    }
    //throw new NotImplemented();
    for (var possession:possessions){
       sommVComptable += possession.getValeurComptable();
    }
    return sommVComptable;
  }
}
