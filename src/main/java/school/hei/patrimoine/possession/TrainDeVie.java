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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long differenceEnMois = ChronoUnit.MONTHS.between(debut, fin);
    int depensesTotales = depensesMensuelle * (int) differenceEnMois;
    int valeurComptableFutur = (int) (valeurComptable - depensesTotales);

    return new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            fin,
            new Argent(
                    this.nom,
                    tFutur,
                    valeurComptableFutur
            ),
            dateDePonction
    );
  }
}
