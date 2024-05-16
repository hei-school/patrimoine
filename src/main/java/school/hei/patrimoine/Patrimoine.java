package school.hei.patrimoine;

import java.util.HashSet;
import java.util.Iterator;
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
    Set<Possession> possessionsFutures = new HashSet<>();
    Iterator<Possession> iterator = possessions.iterator();
    while (iterator.hasNext()) {
      Possession p = iterator.next();
      possessionsFutures.add(p.projectionFuture(tFutur));
    }
    return new Patrimoine(possesseur, tFutur, possessionsFutures);
  }

}
