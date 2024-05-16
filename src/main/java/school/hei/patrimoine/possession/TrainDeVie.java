package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

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
    Duration duration = Duration.between(this.debut, tFutur);
    long jourJusquAuFutur = duration.toDays();
    long depenseTotales = ((jourJusquAuFutur / 30) * this.depensesMensuelle);
    Argent financeProjetFutur = new Argent(
            this.financePar.nom,
            tFutur,
            (int) (this.financePar.valeurComptable-depenseTotales));
    return new TrainDeVie(
            this.nom,
            this.depensesMensuelle,
            this.debut, this.fin,
            financeProjetFutur,
            this.dateDePonction);
  }

}
