package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

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

      int futureExpenses = calculerDepenses(tFutur);


      return new TrainDeVie(
              getNom(),
              depensesMensuelle,
              debut,
              fin.isBefore(tFutur) ? fin : tFutur,
              financePar,
              dateDePonction
      );
  }
  public int calculerDepenses(Instant tFutur) {

    Instant endDate = fin.isBefore(tFutur) ? fin : tFutur;
    if (endDate.isBefore(debut)) {
      return 0;
    }

    long daysBetween = Duration.between(debut, endDate).toDays();
    int monthsBetween = (int) (daysBetween / 30);
    return monthsBetween * depensesMensuelle;
  }
}
