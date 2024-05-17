package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    int totalDepense = 0;
    LocalDate dateFinacePar = LocalDate.ofInstant(financePar.getT(), ZoneId.systemDefault());
    LocalDate dateDebut = LocalDate.ofInstant(debut, ZoneId.systemDefault());
    LocalDate dateFin = LocalDate.ofInstant(fin, ZoneId.systemDefault());
    while (dateFinacePar.isBefore(LocalDate.ofInstant(tFutur, ZoneId.systemDefault()))) {
      if (isBetweenTwoDate(dateFinacePar, dateDebut, dateFin) && dateFinacePar.getDayOfMonth() == dateDePonction && tFutur.isAfter(debut) && tFutur.isBefore(fin))
        totalDepense += depensesMensuelle;
      dateFinacePar = dateFinacePar.plusDays(1);
    }
    Argent financeFuture = new Argent(financePar.nom, tFutur, totalDepense);
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, financeFuture, dateDePonction);
  }
  private boolean isBetweenTwoDate(LocalDate toVerify, LocalDate startDate, LocalDate endDate) {
    return toVerify.isAfter(startDate) && toVerify.isBefore(endDate);
  }
}
