package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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
    Instant tFinance = financePar.getT();

    if (tFutur.isBefore(tFinance)) {
      throw new RuntimeException("T future provided is before t finance");
    }

    int remainingCountableValue = financePar.getValeurComptable() - depensesMensuelle;

    return new TrainDeVie(
            getNom(),
            depensesMensuelle,
            debut,
            fin,
            new Argent(
                    financePar.getNom(),
                    tFutur,
                    remainingCountableValue
            ),
            dateDePonction
    );
  }
}
