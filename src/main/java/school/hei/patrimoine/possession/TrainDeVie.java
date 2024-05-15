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
    super(nom, null, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public TrainDeVie projectionFuture(Instant tFutur) {
    LocalDate dateDeDebutProjection = financePar.t.atZone(ZoneOffset.UTC).toLocalDate();;
    LocalDate dateDeFinProjection = tFutur.atZone(ZoneOffset.UTC).toLocalDate();;

    int moisPonctionnes = 0;
    while (dateDeDebutProjection.isBefore(dateDeFinProjection)) {
      if (dateDeDebutProjection.getDayOfMonth() == dateDePonction) {
        moisPonctionnes++;
      }
      dateDeDebutProjection = dateDeDebutProjection.plusDays(1);
    }

    int argentRestant = financePar.valeurComptable - (depensesMensuelle * moisPonctionnes);
    Argent nouveauFinancement = new Argent(financePar.nom, tFutur, argentRestant);

    tFutur = (tFutur.isAfter(fin)) ? tFutur : fin;
    return new TrainDeVie(nom, depensesMensuelle, debut, tFutur, nouveauFinancement, dateDePonction);
  }
}
