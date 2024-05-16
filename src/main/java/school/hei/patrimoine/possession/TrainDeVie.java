package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.Instant;


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

  public Possession projectionFuture(Instant tFutur) {

    LocalDate moisDebut = debut.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate moisFin = fin.atZone(ZoneId.systemDefault()).toLocalDate();
    Period duree = Period.between(moisDebut, moisFin);
    int dureeEnMois = duree.getMonths();
    int depenses = depensesMensuelle * dureeEnMois;

    return new TrainDeVie(getNom(), depenses, debut, tFutur, financePar, dateDePonction);

  public TrainDeVie projectionFuture(Instant tFutur) {

    throw new NotImplemented();
  }

}