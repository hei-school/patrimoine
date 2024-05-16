package school.hei.patrimoine.possession;



import java.time.Instant;
import java.time.LocalDateTime;
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
    super(nom, debut, 0); //TODO: dirty, redesign
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    var moisEntre = ChronoUnit.MONTHS.between(
            LocalDateTime.ofInstant(this.debut, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC"))
    );
    int totalDepenses = depensesMensuelle * (int) moisEntre;
    int valeurDisponible = financePar.getValeurComptable() - totalDepenses;
    return new TrainDeVie(
            this.getNom(),
            depensesMensuelle,
            debut,
            fin,
            new Argent(
                    financePar.getNom(),
                    tFutur,
                    valeurDisponible
            ),
            dateDePonction);
  }
}
