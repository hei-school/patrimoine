package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
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
    super(nom, debut, depensesMensuelle); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long TotalJourEntreDebutEtTFutur = Duration.between(this.debut, tFutur).toDays();
    long depenseTotal = ((TotalJourEntreDebutEtTFutur / 30) * this.depensesMensuelle);
    Argent projectionFuturArgent = new Argent(
            this.financePar.getNom(),
            tFutur,
            (int) (this.financePar.valeurComptable-depenseTotal));
    return new TrainDeVie(
            this.nom,
            this.depensesMensuelle,
            this.debut,
            this.fin,
            projectionFuturArgent,
            this.dateDePonction);
  }
}