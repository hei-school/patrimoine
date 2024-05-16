package school.hei.patrimoine;

import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
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
    int res = 0;
    for (Possession possession : possessions) {
       res += possession.getValeurComptable();
    }
    return res;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    var possessionsFutur = new HashSet<Possession>();
    possessions.forEach(possession -> {
      if (possession instanceof Argent){
        possessionsFutur.add(possession.projectionFuture(tFutur));
      } else if (possession instanceof Materiel){
        possessionsFutur.add(possession.projectionFuture(tFutur));
      }else {
        possessionsFutur.add(possession.projectionFuture(tFutur));
      }
    });
    return new Patrimoine(
            possesseur,
            t,
            possessionsFutur
    );
  }
}
