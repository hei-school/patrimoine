package school.hei.patrimoine.possession;
import lombok.Getter;

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
    super(nom);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isAfter(debut) && tFutur.isBefore(fin)){
      int moisEcoule =(int) Duration.between(debut,tFutur).toDays()/30;
      int depenseTotal = moisEcoule*depensesMensuelle;
      return new TrainDeVie(nom,
              depensesMensuelle,
              debut,
              fin,
              financePar.evolutionAuCoursDutemps(tFutur,depenseTotal),
              dateDePonction
              );
    }
    return null;
  }
}
