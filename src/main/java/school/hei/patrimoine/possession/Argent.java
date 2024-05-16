package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

@Getter
public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Argent projectionFuture(Instant tFutur) {
    return new Argent(getNom(), getT(), getValeurComptable());
}
