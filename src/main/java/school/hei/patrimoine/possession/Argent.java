package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override

  public Possession projectionFuture(Instant tFutur) {
    return new Argent(nom,tFutur,valeurComptable);

  }
}
