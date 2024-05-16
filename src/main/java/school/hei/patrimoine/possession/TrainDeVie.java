package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
      int dateDePonction)   {
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }
  @Override
  public int valeurComptableFuture(Instant tFutur) {
    LocalDate debutDate = this.debut.atZone(ZoneOffset.UTC).toLocalDate();
    LocalDate tFuturDate = tFutur.atZone(ZoneOffset.UTC).toLocalDate();

    if (tFuturDate.isBefore(debutDate)) {
      return this.valeurComptable;
    }

    long moisEntre = ChronoUnit.MONTHS.between(
            debutDate.withDayOfMonth(1),
            tFuturDate.withDayOfMonth(1));

    int valeurFuture = this.financePar.getValeurComptable() - (int) moisEntre * this.depensesMensuelle;
    return Math.max(valeurFuture, 0);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    int valeurFuture = valeurComptableFuture(tFutur);
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, this.financePar, this.dateDePonction);
  }

}

