package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class TrainDeVie extends Possession {
  public TrainDeVie(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
/*
* une nouvelle pr pour demain
* */