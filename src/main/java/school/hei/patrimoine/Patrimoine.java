package school.hei.patrimoine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    int TotalValeurComptable = 0 ;
    if (possessions.isEmpty()) {
      TotalValeurComptable += 0;
    }else{
      for (Possession possession : possessions) {
        TotalValeurComptable += possession.getValeurComptable();
      }
    }
    return TotalValeurComptable;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    return new Patrimoine(
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }
}
