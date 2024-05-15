package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;

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
    super(nom, Instant.now(), depensesMensuelle * nombreMoisEntre(debut, dateDePonction));

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

  private static int nombreMoisEntre(Instant debut, int dateDePonction) {
    int mDebut = debut.atZone(ZoneId.systemDefault()).getMonthValue();
    int mPonction = dateDePonction % 100;

    int differenceMois = mPonction - mDebut;

    if (differenceMois < 0) {
      differenceMois += 12;
    }

    return differenceMois;
  }


  @Override
  public Possession projectionFuture(Instant tFutur) {

    return new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            tFutur,
            financePar,
            dateDePonction
    );
  }

  public Argent financePar() {
    return financePar;

    throw new NotImplemented();

  }
}