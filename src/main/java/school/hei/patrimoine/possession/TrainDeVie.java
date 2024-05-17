package school.hei.patrimoine.possession;

import lombok.Getter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;


public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
  private final int depensesMensuelle;
  @Getter
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
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    LocalDate dateDebut = debut.atZone(defaultZoneId).toLocalDate();
    LocalDate dateFin = tFutur.atZone(defaultZoneId).toLocalDate();
    int totalMonths = (int) ChronoUnit.MONTHS.between(dateDebut, dateFin);
    int totalDepenses = depensesMensuelle * totalMonths;


    Argent projectionFuturArgent = new Argent(
            this.financePar.getNom(),
            tFutur,
            this.financePar.getValeurComptable() - totalDepenses
    );


    return new TrainDeVie(
            this.nom,
            this.depensesMensuelle,
            this.debut,
            this.fin,
            projectionFuturArgent,
            this.dateDePonction
    );
  }
}
