package school.hei.patrimoine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
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
    throw new NotImplemented();
  }
}
