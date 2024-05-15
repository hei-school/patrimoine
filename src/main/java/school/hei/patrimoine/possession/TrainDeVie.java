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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    int valeurComptableFuturDuCompteCourant = (int) (getFinancePar().getValeurComptable() - (getDepensesMensuelle() * nombreDePonction));
    Argent financeFutur = new Argent(getFinancePar().nom, tFutur, valeurComptableFuturDuCompteCourant);

    long nombreDePonction = Duration.between(debut, tFutur).toDays() / 31;
    int depensesFutur = (int) (getDepensesMensuelle() * nombreDePonction);
    return new TrainDeVie(getNom(), depensesFutur, getDebut(), getFin(), financeFutur, getDateDePonction());
  }
}
