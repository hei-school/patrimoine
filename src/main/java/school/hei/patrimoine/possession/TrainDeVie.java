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
    super(nom, null, financePar.getValeurComptable()); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
      long moisEntreDebutEtTfutur = Duration.between(debut, tFutur).toDays() / 31;
      int depensesTotales = (int) (getDepensesMensuelle() * moisEntreDebutEtTfutur);
      int valeurRestante = financePar.getValeurComptable() - depensesTotales;
      Argent futurFinance = new Argent(financePar.getNom(), tFutur, valeurRestante);
      return new TrainDeVie(nom, depensesMensuelle, debut, fin, futurFinance, dateDePonction);
    }

  }







