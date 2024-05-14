package school.hei.patrimoine.possession;

import java.time.Instant;

public final class Materiel extends Possession {
  public Materiel(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }
}
