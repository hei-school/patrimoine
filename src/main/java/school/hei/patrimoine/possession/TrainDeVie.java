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
    int rest = financePar.getValeurComptable();
    if (tFutur.isAfter(debut) && (financePar.getT().equals(debut) || financePar.getT().isAfter(debut)) && (financePar.getT().equals(fin) || financePar.getT().isBefore(fin))) {

      LocalDate StartLocalDate = debut.atZone(ZoneId.of("UTC")).toLocalDate();
      LocalDate FuturLocalDate = tFutur.atZone(ZoneId.of("UTC")).toLocalDate();

      while (StartLocalDate.isBefore(FuturLocalDate)) {
        if (StartLocalDate.getDayOfMonth() == dateDePonction) {
          rest -= depensesMensuelle;
        }
        StartLocalDate = StartLocalDate.plusDays(1);
      }
    }

    Argent newFinancePar = new Argent(financePar.nom, tFutur, rest);
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, newFinancePar, dateDePonction);
  }
}
