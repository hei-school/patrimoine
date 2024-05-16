package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
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
    super(nom, Instant.now(), financePar.getValeurComptable());
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Duration duree = Duration.between(debut, tFutur);
    long moisFutur = duree.toDays() / 30;
    int depensesTotales = (int) moisFutur * depensesMensuelle;
    int valeurComptableFuture = financePar.getValeurComptable() - depensesTotales;
    Argent projectFuturFinance = new Argent(financePar.getNom(), tFutur, valeurComptableFuture);

    return new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            fin,
            projectFuturFinance,
            dateDePonction);
    }
}
