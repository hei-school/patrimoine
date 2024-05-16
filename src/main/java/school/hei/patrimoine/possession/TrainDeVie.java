package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    long dureeEntreDebutEtTFutur = ChronoUnit.DAYS.between(debut, tFutur);
    long dureeEntreDebutEtTFin = ChronoUnit.DAYS.between(debut, fin);
    double ratioEntreLesDurees = (double) dureeEntreDebutEtTFutur / dureeEntreDebutEtTFin;

    int depensesTotalesDurantLeTrainDeVie = (int) (ratioEntreLesDurees * depensesMensuelle);

    Argent argentFutur = financePar.projectionFuture(tFutur);

    return new TrainDeVie(
            nom,
            depensesTotalesDurantLeTrainDeVie,
            debut,
            fin,
            argentFutur,
            dateDePonction
    );
  }
}
