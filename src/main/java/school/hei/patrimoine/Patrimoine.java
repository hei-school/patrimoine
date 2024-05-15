package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    int valeurComptableTotale = 0;
    for (Possession possession : possessions) {
      valeurComptableTotale += possession.getValeurComptable();
    }
    return valeurComptableTotale;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
