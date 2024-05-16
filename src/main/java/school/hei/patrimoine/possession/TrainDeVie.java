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

    super(nom);

    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Argent nouvelleValeurArgent=new Argent(financePar.getNom(), tFutur,financePar.valeurComptableFuture(tFutur));

 return new TrainDeVie(getNom(),depensesMensuelle,debut,fin,nouvelleValeurArgent,dateDePonction);
  }
}
