package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class TrainDeVie extends Possession {
  private final Instant fin;
  private final Argent financePar;
  private final int dateDePonction;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      int dateDePonction) {
    super(nom, debut, depensesMensuelle);
    this.fin = fin;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
