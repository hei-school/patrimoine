package school.hei.patrimoine.possession;

import java.time.Instant;

import lombok.Getter;

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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    //TODO: condition, dateDePonction is passed
    int projectionValeurComptable = getFinancePar().getValeurComptable() - this.getDepensesMensuelle();
    var projectionFinancerPar = new Argent(getFinancePar().getNom(), tFutur, projectionValeurComptable);

    return new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            fin,
            projectionFinancerPar,
            dateDePonction
    );
  }
}
