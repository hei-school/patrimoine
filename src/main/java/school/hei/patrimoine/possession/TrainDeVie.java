package school.hei.patrimoine.possession;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.Instant;

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
  public Possession projectionFuture(Instant tFutur) {

    LocalDate moisDebut = debut.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate moisFin = fin.atZone(ZoneId.systemDefault()).toLocalDate();
    Period duree = Period.between(moisDebut, moisFin);
    int dureeEnMois = duree.getMonths();
    int depenses = depensesMensuelle * dureeEnMois;

    return new TrainDeVie(getNom(), depenses, debut, tFutur, financePar, dateDePonction);
  }

}