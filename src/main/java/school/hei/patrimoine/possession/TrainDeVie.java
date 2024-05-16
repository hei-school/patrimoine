package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public final class TrainDeVie extends Possession {
  private final Instant debut;
  private final Instant fin;
  @Getter
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
    super(nom, null, 0);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    ZonedDateTime dateDeDebutPonctionZone = debut.atZone(ZoneId.systemDefault());
    ZonedDateTime dateDeFinPonctionZone = tFutur.atZone(ZoneId.systemDefault());

    Argent argentFuture = new Argent(financePar.getNom(), tFutur, (financePar.valeurComptable - depensesMensuelle) / 30 * (dateDeFinPonctionZone.getDayOfMonth() - dateDeDebutPonctionZone.getDayOfMonth()));

    return new TrainDeVie(getNom(), depensesMensuelle, debut, fin, argentFuture, dateDePonction);
  }


}
