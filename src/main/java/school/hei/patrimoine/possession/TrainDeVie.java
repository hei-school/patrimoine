package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

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
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    double monthsBetween = ChronoUnit.MONTHS.between(debut, tFutur);
    int totalDepenses = (int) monthsBetween * depensesMensuelle;
    int nouvelleValeur = financePar.getValeurComptable() - totalDepenses;

    if (nouvelleValeur < 0) {
      throw new IllegalStateException("Insufficient funds in financePar account");
    }

    Argent futureFinancePar = new Argent(financePar.getNom(), tFutur, nouvelleValeur);
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, futureFinancePar, this.dateDePonction);
  }
}