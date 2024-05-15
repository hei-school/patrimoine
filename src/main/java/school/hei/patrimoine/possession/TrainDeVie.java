package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Duration;

import java.time.Instant;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  @Getter
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
    long jourJusquaTFutur = Duration.between(this.debut, tFutur).toDays();
    long depenseTotal = ((jourJusquaTFutur / 30) * this.depensesMensuelle);
    Argent projectFuturDeLaFinance = new Argent(this.financePar.getNom(), tFutur, (int) (this.financePar.valeurComptable-depenseTotal));
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, projectFuturDeLaFinance, this.dateDePonction);
  }
}
