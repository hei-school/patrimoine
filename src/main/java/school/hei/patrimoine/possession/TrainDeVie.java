package school.hei.patrimoine.possession;


import java.time.Instant;
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

    long NbMois = ChronoUnit.MONTHS.between(debut, fin);
    int dépensesTotales = (int) NbMois * depensesMensuelle;
    Argent FutureProjectFinance =  new Argent(financePar.getNom(), tFutur, financePar.valeurComptable - dépensesTotales);
    return new TrainDeVie(nom,
            depensesMensuelle,
            debut,
            fin,
            FutureProjectFinance,
            dateDePonction);
  }
}
