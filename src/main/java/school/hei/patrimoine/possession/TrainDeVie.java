package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
    super(nom, Instant.now(), financePar.getValeurComptable());
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    int restant = financePar.getValeurComptable();
    if ( tFutur.isAfter(debut) &&
         (financePar.getT().isAfter(debut) || financePar.getT().equals(debut)) &&
         (financePar.getT().isBefore(fin) || financePar.getT().equals(fin)) ) {
      LocalDate debutToLocalDate = LocalDate.ofInstant(debut, ZoneId.of("UTC"));
      LocalDate tFuturToLocalDate = LocalDate.ofInstant(tFutur, ZoneId.of("UTC"));
      while (debutToLocalDate.isBefore(tFuturToLocalDate)) {
        if (debutToLocalDate.getDayOfMonth() == dateDePonction) restant -= depensesMensuelle;
        debutToLocalDate = debutToLocalDate.plusDays(1);
      }
    }
    Argent newFinancePar = new Argent(financePar.nom, tFutur, restant);
    return new TrainDeVie( nom, depensesMensuelle, debut, fin, newFinancePar, dateDePonction);
  }
}
