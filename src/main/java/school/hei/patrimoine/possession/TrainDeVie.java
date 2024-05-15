package school.hei.patrimoine.possession;


import lombok.Getter;

import java.time.Instant;
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
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long differenceJour = ChronoUnit.DAYS.between(debut, tFutur);
    long moisEntre = differenceJour / 30;
    int depensesTotales = (int) (moisEntre * depensesMensuelle);
    int valeurRestante = financePar.getValeurComptable() - depensesTotales;
    Argent financeFuture = new Argent(financePar.getNom(), tFutur, valeurRestante);
    return new TrainDeVie( getNom(), depensesMensuelle, debut, fin, financeFuture, dateDePonction);
  }
  @Override
  public Argent getFinancePar() {
    long differenceJour = ChronoUnit.DAYS.between(debut, Instant.now());
    long moisEntre = differenceJour / 30;
    int depensesTotales = (int) (moisEntre * depensesMensuelle);
    int valeurRestante = financePar.getValeurComptable() - depensesTotales;
    return new Argent(financePar.getNom(), Instant.now(), valeurRestante);
  }
}
