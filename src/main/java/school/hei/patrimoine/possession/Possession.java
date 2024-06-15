package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public sealed abstract class Possession implements Serializable /*note(no-serializable)*/ permits
    Argent, Materiel, FluxArgent, GroupePossession {
  protected final String nom;
  protected final Instant t;
  protected final int valeurComptable;

  public final int valeurComptableFuture(Instant tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(Instant tFutur);
}
