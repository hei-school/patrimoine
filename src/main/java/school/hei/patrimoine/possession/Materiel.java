package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZonedDateTime anneeFutur = tFutur.atZone(ZoneId.systemDefault());
    ZonedDateTime anneePresent = t.atZone(ZoneId.systemDefault());

    double moyenneAnnee = anneeFutur.getYear() - anneePresent.getYear();

    int valeurComptableFuture =  (int) (valeurComptable*(moyenneAnnee*tauxDAppreciationAnnuelle));
    return new Materiel(nom, tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
