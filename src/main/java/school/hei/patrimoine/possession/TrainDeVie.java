package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    ZoneId zoneId = ZoneId.systemDefault();
    LocalDate dateDeDebut =  financePar.getT().atZone(zoneId).toLocalDate();
    LocalDate dateFinTime = tFutur.atZone(zoneId).toLocalDate();
    int valeurFuture = financePar.getValeurComptable();

    while (dateDeDebut.isBefore(dateFinTime) || dateDeDebut.isEqual(dateFinTime)) {
      if (dateDeDebut.getDayOfMonth() == dateDePonction) {
        valeurFuture -= depensesMensuelle;
      }
      dateDeDebut = dateDeDebut.plusDays(1);
    }

    int valeurDefinitive = Math.max(valeurFuture, 0);
    Argent soldeDuFinancement = new Argent(financePar.getNom(),tFutur,valeurDefinitive);
    Instant dateFin = tFutur.isBefore(fin) ? fin : tFutur ;

    return new TrainDeVie(nom, depensesMensuelle, debut, dateFin, soldeDuFinancement, dateDePonction);

  }
}
