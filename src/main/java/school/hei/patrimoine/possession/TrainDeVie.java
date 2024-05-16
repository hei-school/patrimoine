package school.hei.patrimoine.possession;

import lombok.Getter;
import lombok.Setter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
@Getter
public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  private final Argent financePar;
  private final Instant dateDePonction;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      Instant dateDePonction) {
    super(nom, debut, financePar.valeurComptable);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
       int valeur_vie_etudiant_future = getValeurComptable() - depensesMensuelle;
       var financeur = new Argent(nom,debut, valeur_vie_etudiant_future);
       return new TrainDeVie(
               nom,
               depensesMensuelle,
               debut,
               fin,
               financeur,
               tFutur
       );
  }
}
