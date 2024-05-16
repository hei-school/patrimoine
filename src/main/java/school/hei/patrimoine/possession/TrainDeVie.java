package school.hei.patrimoine.possession;

import java.time.Instant;
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
    super(nom, debut, financePar.getValeurComptable());
    this.debut = debut;
    this.fin = fin;
    this.depensesMensuelle = depensesMensuelle;
    this.financePar = financePar;
    this.dateDePonction = dateDePonction;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Instant finProjetee = tFutur.isAfter(fin) ? fin : tFutur;

    boolean moisEstPonctionnee = finProjetee.atZone(ZoneId.systemDefault()).getDayOfMonth() >= (dateDePonction);

    int nombreDeMois = (int) ChronoUnit.MONTHS.between(this.debut.atZone(ZoneId.systemDefault()),
            finProjetee.atZone(ZoneId.systemDefault()));

    if (moisEstPonctionnee && nombreDeMois > 1){
      nombreDeMois--;
    } else {
      nombreDeMois = 1;
    }

    int depenseProjetee = financePar.getValeurComptable() - (depensesMensuelle * nombreDeMois);
    Argent futureFinancePar = new Argent(this.financePar.getNom(), finProjetee, depenseProjetee);

    return new TrainDeVie(this.nom, this.depensesMensuelle, this.debut, finProjetee, futureFinancePar, this.dateDePonction);
  }
}
