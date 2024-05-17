package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  private final int depensesMensuelle;
  @Getter
  private final Argent financePar;
  private final int dateDePonction;

  public TrainDeVie(
      String nom,
      int depensesMensuelle,
      Instant debut,
      Instant fin,
      Argent financePar,
      int dateDePonction) {
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
    ZonedDateTime nombreDeJourDebut = debut.atZone(ZoneId.systemDefault());
    ZonedDateTime nombreDeJourFin = tFutur.atZone(ZoneId.systemDefault());

    int moyenneDebutFin = nombreDeJourFin.getDayOfMonth() - nombreDeJourDebut.getDayOfMonth();

    Argent argentFuture = new Argent(financePar.nom, tFutur, (financePar.valeurComptable-depensesMensuelle) / 30 * moyenneDebutFin);
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, argentFuture, dateDePonction);
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
}
