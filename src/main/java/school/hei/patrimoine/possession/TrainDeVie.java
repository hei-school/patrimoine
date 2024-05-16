package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

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
    boolean tFuturEstApresFin = tFutur.isAfter(fin);
    LocalDate debutPonctionnement = LocalDate.ofInstant(debut, ZoneId.systemDefault());
    LocalDate finPonctionnement = LocalDate.ofInstant(tFuturEstApresFin ? fin : tFutur, ZoneId.systemDefault());
    long nombreMoisEntreTetTFutur =  ChronoUnit.MONTHS.between(
            debutPonctionnement.withDayOfMonth(1),
            finPonctionnement.withDayOfMonth(1)
    );
    if(finPonctionnement.getDayOfMonth() < dateDePonction)
      nombreMoisEntreTetTFutur--;

    int totalDepense = depensesMensuelle * (int) nombreMoisEntreTetTFutur;
    return new TrainDeVie(
            nom,
            tFuturEstApresFin ? 0 : depensesMensuelle,
            debut,
            fin,
            new Argent(financePar.nom, tFutur, financePar.valeurComptable - totalDepense),
            dateDePonction
    );
  }
}
