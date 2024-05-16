package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
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
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long joursJusquAuFutur = Duration.between(this.debut, tFutur).toDays();
    long depensesTotales = ((joursJusquAuFutur / 30) * this.depensesMensuelle);
    Argent projectionFinanciereFutur = new Argent(this.financePar.getNom(), tFutur, (int) (this.financePar.valeurComptable-depensesTotales));
    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, this.fin, projectionFinanciereFutur, this.dateDePonction);
  }
}
