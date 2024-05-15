package school.hei.patrimoine.possession;

import java.time.*;

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
      throw new RuntimeException("Future t provided is before finance t");
    }

    Instant appliedT = LocalDateTime.ofInstant(financeT, ZoneId.systemDefault())
            .plusMonths(1)
        .withDayOfMonth(dateDePonction)
    .toInstant(ZoneOffset.ofHours(3));

    int remainingAmount = financePar.getValeurComptable() - depensesMensuelle;

    return new TrainDeVie(
            getNom(),
            depensesMensuelle,
            debut,
            fin,
            new Argent(
                    financePar.getNom(),
                    appliedT,
                    remainingAmount
            ),
            dateDePonction
    );
  }
}
