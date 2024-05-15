package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;

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
    super(nom, debut, financePar.getValeurComptable());
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Instant EndingThink;
    if (tFutur.isAfter(fin)) {
      EndingThink = fin;
    } else {
      EndingThink = tFutur;
    }

    boolean MonthPonctionned = EndingThink.atZone(ZoneId.systemDefault()).getDayOfMonth() >= dateDePonction;
    int numberofmonth = (int) ChronoUnit.MONTHS.between(this.debut.atZone(ZoneId.systemDefault()),EndingThink.atZone(ZoneId.systemDefault()));

    if (!MonthPonctionned){
      numberofmonth--;
    }

    int Depense = financePar.getValeurComptable() - (depensesMensuelle * numberofmonth);
    Argent futureFinancePar = new Argent(this.financePar.getNom(), EndingThink, Depense);

    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, EndingThink, futureFinancePar, this.dateDePonction);
  }
}
