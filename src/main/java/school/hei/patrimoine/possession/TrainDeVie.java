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
    LocalDate debutPonction = financePar.getT().atZone(ZoneOffset.UTC).toLocalDate();
    LocalDate finPonction = tFutur.atZone(ZoneOffset.UTC).toLocalDate();

    int valeurPonction = 0;

    while (debutPonction.isAfter(finPonction)) {
      debutPonction = debutPonction.plusDays(1);

      if (finPonction.getDayOfMonth() == dateDePonction) {
        valeurPonction += depensesMensuelle;
      }
    }

    int resteArgent = financePar.getValeurComptable() - valeurPonction;
    Argent nouvelleFinance = new Argent(financePar.getNom(), tFutur, resteArgent);

    Instant nouveauFinDePonction = tFutur.isBefore(fin) ? fin : tFutur;

    return new TrainDeVie(
            nom,
            depensesMensuelle,
            debut,
            nouveauFinDePonction,
            nouvelleFinance,
            dateDePonction
    );
  }
}
