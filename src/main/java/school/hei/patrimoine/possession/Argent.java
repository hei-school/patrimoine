package school.hei.patrimoine.possession;

import java.time.Instant;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    return new Argent(this.nom, tFutur, this.valeurComptable);
  }
}
