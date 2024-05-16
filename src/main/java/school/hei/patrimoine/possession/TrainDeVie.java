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
  private static final ZoneId defaultZoneId = ZoneId.systemDefault();

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
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    boolean tFuturEstApresFin = tFutur.isAfter(fin);
    LocalDate debutPonction = LocalDate.ofInstant(debut, defaultZoneId);
    LocalDate finPonction = LocalDate.ofInstant(tFuturEstApresFin ? fin : tFutur, defaultZoneId);
    long moisEntreDebutEtFin = ChronoUnit.MONTHS.between(
            debutPonction.withDayOfMonth(1),
            finPonction.withDayOfMonth(1)
    );

    if (finPonction.getDayOfMonth() < dateDePonction) {
      moisEntreDebutEtFin--;
    }

    int depensesTotales = depensesMensuelle * (int) moisEntreDebutEtFin;
    int valeurComptable = financePar.getValeurComptable() - depensesTotales;
    Argent argentProjection = new Argent(financePar.getNom(), tFutur, valeurComptable);

    return new TrainDeVie(
            nom,
            tFuturEstApresFin ? 0 : depensesMensuelle,
            debut,
            fin,
            argentProjection,
            dateDePonction
    );
  }
}
