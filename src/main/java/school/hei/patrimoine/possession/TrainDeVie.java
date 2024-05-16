package school.hei.patrimoine.possession;

import java.time.Duration;
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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isBefore(debut) || tFutur.isAfter(fin)) {
      return new TrainDeVie(nom, depensesMensuelle, debut, fin, financePar, dateDePonction);
    }

    long secondes = Duration.between(debut, tFutur).getSeconds();
    // Convertir les secondes en mois
    long mois = (long) (secondes / (30.44 * 24 * 3600));

    int totalDepenses = (int) (mois * depensesMensuelle);
    Argent nouveauFinancePar = new Argent(financePar.getNom(), tFutur, financePar.getValeurComptable() - totalDepenses);

    return new TrainDeVie(nom, depensesMensuelle, debut, fin, nouveauFinancePar, dateDePonction);
  }
}
