package school.hei.patrimoine.possession;

import java.time.Duration;
import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

@Getter
public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  private final Argent financePar;
  private final int dateDePonction;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      int dateDePonction) {
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    Instant nouveauDebut = debut.plus(Duration.between(debut, tFutur));
    Instant nouveauFin = fin.plus(Duration.between(fin, tFutur));
    return new TrainDeVie(nom, depensesMensuelle, nouveauDebut, nouveauFin, financePar, dateDePonction);
  }

}
