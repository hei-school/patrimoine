package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneOffset;

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
    if (tFutur.isBefore(debut) || tFutur.isAfter(fin)) {
      System.out.println("L'instant futur n'est pas dans la plage de validité du train de vie: ");
      return new TrainDeVie(nom, 0, debut, fin, financePar, dateDePonction);
    }
    int debutYear = financePar.getT().atZone(ZoneOffset.UTC).getYear();
    int debutMonth = financePar.getT().atZone(ZoneOffset.UTC).getMonthValue();
    int tFuturYear = tFutur.atZone(ZoneOffset.UTC).getYear();
    int tFuturMonth = tFutur.atZone(ZoneOffset.UTC).getMonthValue();


    long nombreDeMois = (tFuturYear - debutYear) * 12 + (tFuturMonth - debutMonth);
    int depensesFutures = (int) nombreDeMois * depensesMensuelle;
    var financeParFutures = new Argent(nom, tFutur, financePar.valeurComptable - depensesFutures);

    return new TrainDeVie(nom, depensesFutures, debut, fin, financeParFutures, dateDePonction);
  }
}
