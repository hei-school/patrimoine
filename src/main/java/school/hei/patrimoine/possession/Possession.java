package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
@ToString
@AllArgsConstructor
@Getter
public sealed abstract class Possession permits
    Argent, Materiel, TrainDeVie {
  protected final String nom;
  protected final Instant t;
  protected final int valeurComptable;

  public final int valeurComptableFuture(Instant tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }
  public final int getFinance(Argent argent1,Argent argent) {
    return (argent1.getValeurComptable()+argent.getValeurComptable());
  }

  public abstract Possession projectionFuture(Instant tFutur);
}
