package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
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
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, this.financePar, this.dateDePonction);
  }

  TrainDeVie projectionFuture(Instant tFutur, Argent nouveauFinancement) {
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, nouveauFinancement, this.dateDePonction);
  }

  public int financementsFutur(Instant tFutur) {
    Calendar calendarDebut = Calendar.getInstance();
    calendarDebut.setTime(Date.from(debut));

    int nombreDeMois = 0;
    while (calendarDebut.getTime().before(Date.from(tFutur))) {
      if (calendarDebut.get(Calendar.DAY_OF_MONTH) == dateDePonction) {
        nombreDeMois++;
      }
      calendarDebut.add(Calendar.DATE, 1);
    }

    return depensesMensuelle * nombreDeMois;
  }
}
