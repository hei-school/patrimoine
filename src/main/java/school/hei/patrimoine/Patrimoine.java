package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

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

  public Patrimoine projectionFuture(Instant tFutur) {
    return new Patrimoine(
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }
}
