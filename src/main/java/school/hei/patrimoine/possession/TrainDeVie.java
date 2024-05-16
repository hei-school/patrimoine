package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    Instant commencement = (LocalDate.ofInstant(debut, ZoneId.systemDefault()).getDayOfMonth() == getDateDePonction())
            ? debut : debut.plus(1, ChronoUnit.MONTHS);
    tFutur = (tFutur.isAfter(getFin())) ? tFutur : getFin();
    long nombreDePonction = (ChronoUnit.DAYS.between(commencement, tFutur) / 31);
    System.out.println(nombreDePonction);
    int argentRestant = (int) (getFinancePar().getValeurComptable() - (getDepensesMensuelle() * nombreDePonction));
    Argent financeFutur = new Argent(getFinancePar().nom, tFutur, argentRestant);
    return new TrainDeVie(getNom(), getDepensesMensuelle(), getDebut(), getFin(), financeFutur, getDateDePonction());
  }
}
