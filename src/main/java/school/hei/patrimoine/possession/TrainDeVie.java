package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
@Getter
public final class TrainDeVie extends Possession {
  public TrainDeVie(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
