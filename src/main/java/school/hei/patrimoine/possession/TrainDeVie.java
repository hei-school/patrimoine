package school.hei.patrimoine.possession;

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
    super(nom, Instant.now(), depensesMensuelle * nbMoisEntre(debut, dateDePonction));
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  private static int nbMoisEntre(Instant debut, int dateDePonction) {
      int moisDebut = debut.atZone(ZoneId.systemDefault()).getMonthValue();
      int moisPonction = dateDePonction % 100;

      int differenceMois = moisPonction - moisDebut;

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
  }
}
