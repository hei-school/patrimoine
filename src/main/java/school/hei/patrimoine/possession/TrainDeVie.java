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
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, this.financePar, this.dateDePonction);
  }

  TrainDeVie projectionFuture(Instant tFutur, Argent nouveauFinancement) {
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, nouveauFinancement, this.dateDePonction);
  }

  public int financementsFutur(Instant tFutur) {
    long nombreDeMois = Duration.between(debut, tFutur).toDays() / 30;
    return (int) (depensesMensuelle * nombreDeMois);
  }
}
