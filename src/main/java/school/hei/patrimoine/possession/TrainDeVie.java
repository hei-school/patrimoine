package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
          int dateDePonction
  ) {
    super(nom, debut, financePar.valeurComptable);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZonedDateTime nombreDeJourDebut = debut.atZone(ZoneId.systemDefault());
    ZonedDateTime nombreDeJourFin = tFutur.atZone(ZoneId.systemDefault());

    int moyenneDebutFin = nombreDeJourFin.getDayOfMonth() - nombreDeJourDebut.getDayOfMonth();

    Argent argentFuture = new Argent(financePar.getNom(), tFutur, (financePar.valeurComptable-depensesMensuelle) / 30 * moyenneDebutFin);
    return new TrainDeVie(getNom(), depensesMensuelle, debut, fin, argentFuture, dateDePonction);
  }

  public Argent getFinancePar() {
    return financePar;
  }
}
