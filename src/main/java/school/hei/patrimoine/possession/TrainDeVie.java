package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.*;


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
    super(nom, debut, depensesMensuelle);
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.dateDePonction = dateDePonction;

    this.financePar = financePar;
    this.financePar.addFinancés(this);
  }

  @Override

  public Possession projectionFuture(Instant tFutur) {
    if (tFutur.isBefore(debut) || tFutur.isAfter(fin)) {
      return this;
    } else {
      long months = Duration.between(debut, tFutur).toDays() / 30;
      int newWithdrawalDate = dateDePonction + (int) months;
      long monthsDuration = Duration.between(debut, tFutur).toDays() / 30;
      int totalExpenses = depensesMensuelle * (int) monthsDuration;

      return new TrainDeVie(
              nom,
              depensesMensuelle,
              debut,
              fin,
              financePar,
              newWithdrawalDate);
    }


    public TrainDeVie projectionFuture (Instant tFutur){
      LocalDate dateDeDebut = this.financePar.t.atZone(ZoneOffset.UTC).toLocalDate();
      LocalDate dateDeFin = tFutur.atZone(ZoneOffset.UTC).toLocalDate();
      int nombreDeDatesDePonctionsPassees =
              (int)
                      dateDeDebut
                              .datesUntil(dateDeFin.plusDays(1))
                              .filter(d -> d.getDayOfMonth() == dateDePonction)
                              .count();

      int nouvelleValeurComptable =
              financePar.getValeurComptable() - (depensesMensuelle * nombreDeDatesDePonctionsPassees);
      var financementRestant = new Argent(nom, tFutur, nouvelleValeurComptable);

      tFutur = (tFutur.isAfter(fin)) ? tFutur : fin;
      return new TrainDeVie(nom, depensesMensuelle, debut, tFutur, financementRestant, dateDePonction);
    }
  }

}