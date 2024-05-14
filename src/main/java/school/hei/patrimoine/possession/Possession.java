package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public sealed abstract class Possession permits
    Argent, Materiel, TrainDeVie {
  protected final String nom;
  protected final Instant t;
  protected final int valeurComptable;

  public abstract int valeurComptableFuture(Instant tFutur);

}
