package school.hei.patrimoine.possession;

import lombok.Getter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    public TrainDeVie projectionFuture(Instant tFutur) {
      double dureeMois = ChronoUnit.MONTHS.between(debut, tFutur);
      int totalDepenses = (int) dureeMois * depensesMensuelle;

      return new TrainDeVie(this.nom,
              this.depensesMensuelle,
              this.debut,
              this.fin,
              financePar,
              this.dateDePonction);
    }
  }
