package school.hei.patrimoine.possession;

import java.time.Instant;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  private final Argent financePar;
  private final int dateDePonction;

  private final FluxArgent fluxArgent;

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

    this.fluxArgent = new FluxArgent(nom, depensesMensuelle, debut, fin, financePar, dateDePonction);
  }

  private TrainDeVie(FluxArgent fluxArgent) {
    super(fluxArgent.getNom(), null, 0);
    this.debut = fluxArgent.getDebut();
    this.fin = fluxArgent.getFin();
    this.depensesMensuelle = fluxArgent.getFluxMensuel();
    this.dateDePonction = fluxArgent.getDateOperation();
    this.financePar = fluxArgent.getArgent();

    this.fluxArgent = fluxArgent;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    return new TrainDeVie(fluxArgent.projectionFuture(tFutur));
  }
}