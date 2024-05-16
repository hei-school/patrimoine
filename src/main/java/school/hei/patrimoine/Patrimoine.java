package school.hei.patrimoine;

import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Patrimoine(Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    return possessions.stream()
            .mapToInt(Possession::getValeurComptable)
            .sum();
  }


  public Patrimoine projectionFuture(Instant tFutur) {
    var possessionsFutur = new HashSet<Possession>();
    for (var possession : possessions) {
      possessionsFutur.add(projectionFutureForPossession(possession, tFutur));
    }
    return new Patrimoine(
            possesseur,
            tFutur,
            possessionsFutur
    );
  }

  private Possession projectionFutureForPossession(Possession possession, Instant tFutur) {
    if (possession instanceof Argent || possession instanceof Materiel || possession instanceof TrainDeVie) {
      return possession.projectionFuture(tFutur);
    } else {
      return possession;
    }
  }
}
