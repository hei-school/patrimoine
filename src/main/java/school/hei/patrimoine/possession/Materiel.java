package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
   Instant instant = Instant.now();
    LocalDate present = LocalDate.ofInstant(instant, ZoneId.systemDefault());
    LocalDate future = LocalDate.ofInstant(tFutur,ZoneId.systemDefault());
    int annee = (future.getYear()-present.getYear())/365;
   double totalTauxAppreciation = 0;
    for (int i = 0; i < annee; i++) {
      totalTauxAppreciation = totalTauxAppreciation+tauxDAppreciationAnnuelle;
    }
    int nouvelValeur = (int) (valeurComptable * totalTauxAppreciation);

    return  new Materiel(
            nom,
            t,
            nouvelValeur,
            totalTauxAppreciation
    );
  }
}
