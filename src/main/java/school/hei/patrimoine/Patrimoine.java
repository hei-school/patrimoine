import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {


    if (possessions.isEmpty()) {
      return 0;


    }
    throw new NotImplemented();
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    Set<Possession> possessionsFuture = new HashSet<>();
    possessions.forEach(possession -> {


        possessionsFuture.add(
                possession.projectionFuture(tFutur)


        );
    });
    return new Patrimoine(possesseur, tFutur, possessions);
  }
}
