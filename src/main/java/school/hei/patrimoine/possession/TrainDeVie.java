package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    if (tFutur.isBefore(debut) || tFutur.isAfter(fin)) {
      throw new IllegalArgumentException("La date fournie doit être comprise entre le début et la fin du train de vie.");
    }
    ZonedDateTime zonedDebut = debut.atZone(ZoneId.systemDefault());
    ZonedDateTime zonedDatePonction = zonedDebut.plus(1, ChronoUnit.MONTHS)
            .withDayOfMonth(dateDePonction);
    int soldeRestant = financePar.getValeurComptable() - depensesMensuelle;
    Argent financeFutur = new Argent(financePar.getNom(), zonedDatePonction.toInstant(), soldeRestant);
    return new TrainDeVie(getNom(), depensesMensuelle, debut, fin, financeFutur, dateDePonction);
  }

}
