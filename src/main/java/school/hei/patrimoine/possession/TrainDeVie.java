package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

  public Instant getDebut() {
    return debut;
  }

  public Instant getFin() {
    return fin;
  }

  public int getDepensesMensuelle() {
    return depensesMensuelle;
  }

  public Argent getFinancePar() {
    return financePar;
  }

  public int getDateDePonction() {
    return dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isBefore(debut)) {
      return this;
    } else if (tFutur.isAfter(fin)) {
      return this;
    } else {
      long mois = ChronoUnit.MONTHS.between(debut, tFutur);
      int nouvelleDateDePonction = dateDePonction + (int) mois;

      return new TrainDeVie(nom, depensesMensuelle, debut, fin, financePar, nouvelleDateDePonction);
    }
  }
}
