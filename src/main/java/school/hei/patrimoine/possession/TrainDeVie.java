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
    ZoneOffset UTC = ZoneOffset.UTC;
    LocalDate dateDeDebutPonction = financePar.getT().atZone(UTC).toLocalDate();
    LocalDate dateDeFinPonction = tFutur.atZone(UTC).toLocalDate();

    int valeurPoctioneAccumule = 0;
    while (dateDeFinPonction.isAfter(dateDeDebutPonction)) {
      dateDeDebutPonction = dateDeDebutPonction.plusDays(1);
      if (dateDeDebutPonction.getDayOfMonth() == dateDePonction) {
        valeurPoctioneAccumule += depensesMensuelle;
      }
    }

    int soldeRestant = financePar.getValeurComptable() - valeurPoctioneAccumule;
    Argent nouveauFinancement = new Argent(financePar.getNom(), tFutur, soldeRestant);

    Instant nouveauFin = tFutur.isBefore(fin) ? fin : tFutur;

    return new TrainDeVie(nom, depensesMensuelle, debut, nouveauFin, nouveauFinancement, dateDePonction);
  }
}
