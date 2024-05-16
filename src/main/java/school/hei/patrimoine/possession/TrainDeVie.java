package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
    super(nom, debut, depensesMensuelle);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

    @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    LocalDate debutDate = debut.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate finDate = tFutur.atZone(ZoneId.systemDefault()).toLocalDate();

    int totalMonths = (finDate.getYear() - debutDate.getYear()) * 12 + (finDate.getMonthValue() - debutDate.getMonthValue());
    int totalDepenses = 0;

    for (int i = 0; i <= totalMonths; i++) {
      LocalDate currentPonctionDate = debutDate.plusMonths(i).withDayOfMonth(dateDePonction);
      if (!currentPonctionDate.isBefore(debutDate) && currentPonctionDate.isBefore(finDate)) {
        totalDepenses += depensesMensuelle;
      }
    }

    Argent projectionFuturArgent = new Argent(
            this.financePar.getNom(),
            tFutur,
            this.financePar.valeurComptable - totalDepenses
    );

    return new TrainDeVie(
            this.nom,
            this.depensesMensuelle,
            this.debut,
            this.fin,
            projectionFuturArgent,
            this.dateDePonction
    );
  }
}
