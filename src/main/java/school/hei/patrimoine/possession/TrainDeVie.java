package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Getter
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
    ZoneId zoneId = ZoneId.systemDefault();
    int nombreDeMois = (int) ChronoUnit.MONTHS.between(
      debut.atZone(zoneId).withDayOfMonth(dateDePonction),
      tFutur.atZone(zoneId).withDayOfMonth(dateDePonction));
    int futurValeurDArgent = financePar.valeurComptable - depensesMensuelle * nombreDeMois;
    Argent futurFinancePar = new Argent(financePar.nom, tFutur, futurValeurDArgent);
    return new TrainDeVie(nom, depensesMensuelle, debut, tFutur, futurFinancePar, dateDePonction);
  }
}
