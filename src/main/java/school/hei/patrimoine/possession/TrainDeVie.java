package school.hei.patrimoine.possession;
import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
  private final int depensesMensuelle;
  @Getter
  private final Argent financePar;
  private final int dateDePonction;

  public TrainDeVie(
          String nom,
          int depensesMensuelle,
          Instant debut,
          Instant fin,
          Argent financePar,
          int dateDePonction) {
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    Argent dansLeFuturFinancePar = new Argent(
            this.financePar.getNom(),
            tFutur,
            this.financePar.valeurComptableFuture(tFutur)
    );

    TrainDeVie traindeVieFuture = new TrainDeVie(
            nom,
            depensesMensuelle,
            tFutur,
            fin,
            dansLeFuturFinancePar,
            dateDePonction
    );

    return traindeVieFuture;
  }
}
