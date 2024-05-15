package school.hei.patrimoine.possession;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.w3c.dom.CDATASection;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
@EqualsAndHashCode
@ToString
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
  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isAfter(debut) && tFutur.isBefore(fin)){
      return new TrainDeVie(nom,
              depensesMensuelle,
              tFutur,
              fin,
              financePar,
              dateDePonction
              );
    }
    return null;
  }
}
