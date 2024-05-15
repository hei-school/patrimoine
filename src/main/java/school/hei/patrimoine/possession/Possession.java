package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

@AllArgsConstructor
@Getter
public sealed abstract class Possession permits
    Argent, Materiel, TrainDeVie {
  protected final String nom;
  protected final Instant t;
  protected final int valeurComptable;


  public int valeurComptableFuture(Instant tFutur) {

  public final int valeurComptableFuture(Instant tFutur) {

    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(Instant tFutur);
}
