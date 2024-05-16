package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
    LocalDate debutLocalDate = debut.atZone(ZoneOffset.UTC).toLocalDate();
    LocalDate finLocalDate = tFutur.atZone(ZoneOffset.UTC).toLocalDate();

    long nombreDePonctionsEffectuees = debutLocalDate
            .datesUntil(finLocalDate.plusDays(1))
            .filter(d -> d.getDayOfMonth() == dateDePonction)
            .count();

    int nouvelleValeurComptable = financePar.getValeurComptable() - (depensesMensuelle * (int) nombreDePonctionsEffectuees);
    Argent financementRestant = new Argent(nom, tFutur, nouvelleValeurComptable);

    Instant nouvelleDateFin = (tFutur.isAfter(fin)) ? tFutur : fin;
    return new TrainDeVie(nom, depensesMensuelle, debut, nouvelleDateFin, financementRestant, dateDePonction);
  }
}
