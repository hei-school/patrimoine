package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
    LocalDate dateDebutTrainDeVie = this.financePar.t.atZone(ZoneOffset.UTC).toLocalDate();
    LocalDate dateFinTrainDeVie = tFutur.atZone(ZoneOffset.UTC).toLocalDate();
    int nombreDePonctions = (int) dateDebutTrainDeVie
            .datesUntil(dateFinTrainDeVie.plusDays(1))
            .filter(d -> d.getDayOfMonth() == dateDePonction)
            .count();

    int valeurComptableApresDepenseMensuelle = financePar.getValeurComptable() - (depensesMensuelle * nombreDePonctions);
    var financementApresPonction = new Argent(nom, tFutur, valeurComptableApresDepenseMensuelle);

    tFutur = (tFutur.isAfter(fin)) ? tFutur : fin;
    return new TrainDeVie(nom, depensesMensuelle, debut, tFutur, financementApresPonction, dateDePonction);
  }
}
