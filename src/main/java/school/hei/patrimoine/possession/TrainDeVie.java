package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
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
    super(nom, debut, financePar.getValeurComptable()); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();

    LocalDate StartLocalDate;
    if (debut.isBefore(financePar.getT())) {
      StartLocalDate = financePar.getT().atZone(zoneId).toLocalDate();
    } else {
      StartLocalDate = debut.atZone(zoneId).toLocalDate();
    }

    LocalDate EndLocalDate = tFutur.atZone(zoneId).toLocalDate();

    int MonthBetweenEndandStartLocalDate = (int) ChronoUnit.MONTHS.between(StartLocalDate, EndLocalDate);

    int FuturValues = calculerValeurFuture(MonthBetweenEndandStartLocalDate);

    Argent NewBalance = new Argent(financePar.getNom(),financePar.getT(),FuturValues);

    Instant StartingDate = StartLocalDate.atStartOfDay(zoneId).toInstant();

    return new TrainDeVie(nom,depensesMensuelle,StartingDate,fin,NewBalance,dateDePonction);
  }

  private int calculerValeurFuture(int MonthBetweenEndandStartLocalDate) {
    int FutureValues = financePar.getValeurComptable();
    for (int i = 0 ; i < MonthBetweenEndandStartLocalDate; i ++){
      FutureValues -= depensesMensuelle;
    }
    return FutureValues;
  }
}
