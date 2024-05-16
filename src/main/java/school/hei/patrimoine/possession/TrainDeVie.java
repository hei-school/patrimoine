package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    if(tFutur.isAfter(fin)) {
      return new TrainDeVie(nom, 0, debut, fin, null, dateDePonction);
    }

    ZoneId zoneId = ZoneId.systemDefault();
    LocalDate ajusterLeDebutAvecLaPonction = LocalDate
      .ofInstant(debut, zoneId)
      .withDayOfMonth(dateDePonction);
    LocalDate ajusterLaDateFuturAvecLaPonction = LocalDate
      .ofInstant(tFutur, zoneId)
      .plusDays(1)
      .withDayOfMonth(dateDePonction);

    int nombreDeMois = (int) ChronoUnit.MONTHS.between(ajusterLeDebutAvecLaPonction, ajusterLaDateFuturAvecLaPonction);
    int futurValeurDArgent = financePar.valeurComptable - depensesMensuelle * nombreDeMois;
    Argent futurFinancePar = new Argent(financePar.nom, tFutur, futurValeurDArgent);
    return new TrainDeVie(nom, depensesMensuelle, debut, tFutur, futurFinancePar, dateDePonction);
  }
}
