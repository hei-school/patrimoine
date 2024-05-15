package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    super(nom, debut, depensesMensuelle); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Instant financeT = financePar.getT();
    if (tFutur.isBefore(financeT)) {
      throw new RuntimeException();
    }

    Instant appliedT = financeT.plus(1, ChronoUnit.MONTHS)
            .atZone(ZoneId.systemDefault())
            .withDayOfMonth(dateDePonction)
            .toInstant();

    int remainingBalance = financePar.getValeurComptable() - depensesMensuelle;

    TrainDeVie trainDeVie = new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            fin,
            new Argent(
                    financePar.nom,
                    appliedT,
                    remainingBalance),
            dateDePonction
    );

    return trainDeVie;

  }
}
