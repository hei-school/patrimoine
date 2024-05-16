package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.LocalDateTime;
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
    LocalDateTime dateTime = LocalDateTime.ofInstant(tFutur, ZoneOffset.UTC);
    String ponctionTemplate = String.format("%04d-%02d-%02dT00:00:00Z", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());

    Instant instantDePonctionDuMois = Instant.parse(ponctionTemplate);

    if (tFutur.isAfter(instantDePonctionDuMois)){
      Argent financeParPonctionnee = new Argent(financePar.getNom(), tFutur, financePar.getValeurComptable() - depensesMensuelle);
      return new TrainDeVie(nom, depensesMensuelle, debut, fin, financeParPonctionnee, dateDePonction);
    }
    return new TrainDeVie(nom, depensesMensuelle, debut, fin, financePar, dateDePonction);
  }
}
