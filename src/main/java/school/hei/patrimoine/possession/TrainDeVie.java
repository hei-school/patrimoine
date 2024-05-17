package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDateTime;
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
  var moinsEntre = ChronoUnit.MONTHS.between(
          LocalDateTime.ofInstant(this.debut, ZoneId.of("UTC")),
          LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC"))
  );
  int totalDepenses = depensesMensuelle * (int) moinsEntre;
  int valeurDisponible = financePar.getValeurComptable() - totalDepenses;

  return new TrainDeVie(
          this.getNom(),
          depensesMensuelle,
          debut,
          fin,
          new Argent(
                  financePar.getNom(),
                  tFutur,
                  valeurDisponible
          ),
          dateDePonction
  );
}
}
