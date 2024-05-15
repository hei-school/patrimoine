package school.hei.patrimoine.possession;

import lombok.AllArgsConstructor;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZonedDateTime;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  private final Argent financePar;
  private final int dateDePonction;
  public TrainDeVie (
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      int dateDePonction) {
    super(nom, financePar.getT(), financePar.getValeurComptable()); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZonedDateTime dateFuture = ZonedDateTime.parse(tFutur.toString());
    ZonedDateTime  dateOld = ZonedDateTime.parse(financePar.getT().toString());

    int differenceYear = 0;
    differenceYear = dateFuture.getMonthValue() - dateOld.getMonthValue();

    int newValeurComptable = financePar.getValeurComptable() - (depensesMensuelle * differenceYear);
    Argent newArgent = new Argent(
            getNom(),
            financePar.getT(),
            newValeurComptable
    );

    return new TrainDeVie(
            getNom(),
            depensesMensuelle,
            financePar.getT(),
            fin,
            newArgent,
            dateDePonction
    );
  }
}
