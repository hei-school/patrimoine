package school.hei.patrimoine.possession;


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
    super(nom, null, depensesMensuelle);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
      ZoneId zoneId = ZoneId.systemDefault();
      double moisPassee = (double) ChronoUnit.DAYS.between(debut.atZone(zoneId), fin.atZone(zoneId)) / 12;
      return new TrainDeVie(nom, (int) ((int) depensesMensuelle* (moisPassee)),debut,fin, financePar,dateDePonction);
  }
}
