package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
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

    int totalDepense = 0;
    LocalDate FinancedDay = LocalDate.ofInstant(financePar.getT(), ZoneId.systemDefault());
    LocalDate StartingLocalate = LocalDate.ofInstant(debut, ZoneId.systemDefault());
    LocalDate EndingLocalDate = LocalDate.ofInstant(fin, ZoneId.systemDefault());
    while (FinancedDay.isBefore(LocalDate.ofInstant(tFutur, ZoneId.systemDefault()))) {
      if (isBetweenTwoDate(FinancedDay, StartingLocalate, EndingLocalDate) && FinancedDay.getDayOfMonth() == dateDePonction && tFutur.isAfter(debut) && tFutur.isBefore(fin))
        totalDepense += depensesMensuelle;
      FinancedDay = FinancedDay.plusDays(1);
    }
    Argent financeFuture = new Argent(financePar.nom, tFutur, totalDepense);
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, financeFuture, dateDePonction);
  }

  private boolean isBetweenTwoDate(LocalDate toVerify, LocalDate startDate, LocalDate endDate) {
    return (ChronoUnit.DAYS.between(startDate, toVerify) > 0) && (ChronoUnit.DAYS.between(toVerify, endDate) > 0);
  }
}
