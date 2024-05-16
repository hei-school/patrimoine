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
  public TrainDeVie projectionFuture(Instant tFutur) {
    Duration duree = Duration.between(debut, tFutur);
    long jour_de_futur = duree.toDays();
    int mois_de_futur = (int) (jour_de_futur / 30);
    int depensesTotales = mois_de_futur * depensesMensuelle;
    Argent financeFutur = new Argent(financePar.getNom(), tFutur, financePar.valeurComptable - depensesTotales);

    return new TrainDeVie(nom,
            depensesMensuelle,
            debut,
            fin,
            financeFutur,
            dateDePonction);
  }
}
