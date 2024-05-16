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
    if(!(tFutur.isAfter(debut)) && (tFutur.isBefore(fin))){
      return this ;
    }else {
      long mois = Duration.between(debut, tFutur).toDays() / 30;
      int dateRetrait = (int)mois + dateDePonction;
      double depenseTotal = depensesMensuelle * mois;
    }
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, financePar, dateDePonction);
  }
}
