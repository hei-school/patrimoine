package school.hei.patrimoine.possession;

import java.time.Instant;
import school.hei.patrimoine.NotImplemented;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
