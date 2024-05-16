package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.*;
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

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    if (debut.isAfter(tFutur)){
      throw new RuntimeException();
    }
    LocalDate dateDeDebutDeProjection = debut.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate dateDeFinDeProjection = tFutur.atZone(ZoneId.systemDefault()).toLocalDate();
    long nombreDeMoisPonctionnes = ChronoUnit.MONTHS.between(dateDeDebutDeProjection, dateDeFinDeProjection);
    nombreDeMoisPonctionnes = (dateDeDebutDeProjection.getDayOfMonth() <= dateDePonction)
            ? nombreDeMoisPonctionnes + 1 : nombreDeMoisPonctionnes;
    int valeurComptableFutur = (int) (financePar.getValeurComptable() - (depensesMensuelle * nombreDeMoisPonctionnes));
    Argent argentFuture = new Argent(financePar.getNom(), tFutur, valeurComptableFutur);
    return new TrainDeVie(getNom(), depensesMensuelle, debut, fin, argentFuture, dateDePonction);
  }
}
