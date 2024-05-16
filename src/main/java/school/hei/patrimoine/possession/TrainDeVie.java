package school.hei.patrimoine.possession;
import java.time.Duration;
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
    Instant now = Instant.now();
    Duration duration = Duration.between(now, this.fin);
    long dureeEnSecondes = duration.getSeconds();
    int nbDeMoisRestante = (int) Math.ceil((double) dureeEnSecondes / (30 * 24 * 60 * 60));
    int depensesTotalesFuture = depensesMensuelle * nbDeMoisRestante;
    var financeur = new Argent(nom,debut, depensesTotalesFuture);
    return new TrainDeVie(
            getNom(),
            depensesTotalesFuture,
            debut,
            tFutur,
            financeur,
            dateDePonction);
  }
}
