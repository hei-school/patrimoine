package school.hei.patrimoine;

import java.time.Instant;
import java.util.Set;

public class Patrimoine {
  private final Personne possesseur;
  private final Instant t;
  private final Set<Possession> possessions;

  public Patrimoine(Personne possesseur, Instant t, Set<Possession> possessions) {
    this.possesseur = possesseur;
    this.t = t;
    this.possessions = possessions;
  }

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    throw new RuntimeException("TODO");
  }
}
