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

  public TrainDeVie projectionFuture(Instant tFutur) {
    throw new NotImplemented();

  }
}