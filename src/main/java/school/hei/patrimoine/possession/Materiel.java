package school.hei.patrimoine.possession;

import java.sql.Date;
import java.time.Instant;

public final class Materiel extends Possession {

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
      double tauxDAppreciationAnnuelle = 0.1;
      int futur_valeur = (int) (valeurComptable * tauxDAppreciationAnnuelle);
      return futur_valeur;
     }

  public Possession projectionFuture(Instant tFutur) {
      Date duration = (Date.valueOf(String.valueOf(tFutur)));
    double tauxDAppreciationAnnuelle = 0.1;
    int futur_valeur = (int) (valeurComptable * tauxDAppreciationAnnuelle);
      return new Materiel(nom, t, futur_valeur, tauxDAppreciationAnnuelle);

  }
}
