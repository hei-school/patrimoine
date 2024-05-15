package school.hei.patrimoine.possession;


import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;

import static java.time.temporal.ChronoUnit.MONTHS;

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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
      long duration = (MONTHS.between(debut, fin));
      int futurDepense = (int) (depensesMensuelle * duration);
      return new TrainDeVie(nom, futurDepense, debut, fin, financePar, dateDePonction);
  }
}
