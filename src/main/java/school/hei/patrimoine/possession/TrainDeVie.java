package school.hei.patrimoine.possession;



import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    super(nom, debut, financePar.getValeurComptable()); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();
    LocalDate dateDeDebutLocalDate = (debut.isBefore(financePar.getT()) ? financePar.getT() : debut).atZone(zoneId).toLocalDate();
    LocalDate dateFinLocalDate = tFutur.atZone(zoneId).toLocalDate();
    int moisEntreDebutEtFin = (int) ChronoUnit.MONTHS.between(dateDeDebutLocalDate, dateFinLocalDate);
    int valeurFuture = calculerValeurFuture(moisEntreDebutEtFin);

    Argent nouveauSoldeDuFinancement = new Argent(financePar.getNom(),financePar.getT(),valeurFuture);

    Instant dateDeDebut = dateDeDebutLocalDate.atStartOfDay(zoneId).toInstant();

    return new TrainDeVie(nom,depensesMensuelle,dateDeDebut,fin,nouveauSoldeDuFinancement,dateDePonction);
  }

  private int calculerValeurFuture(int moisEntreDebutEtFin) {
    int valeurFuture = financePar.getValeurComptable();
    for (int i = 0; i < moisEntreDebutEtFin; i++) {
      valeurFuture -= depensesMensuelle;
    }
    return valeurFuture;
  }
}
