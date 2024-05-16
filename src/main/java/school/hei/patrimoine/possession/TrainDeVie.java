package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;

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
    super(nom,null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }
    public Argent getFinancePar() {
        return financePar;
    }
    @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
        return new TrainDeVie(
                nom,
                depensesMensuelle,
                debut,
                tFutur,
                financePar,
                dateDePonction
        );
  }

}
