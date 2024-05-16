package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Materiel projectionFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();
    int nombreDAnnnee = (int) ChronoUnit.YEARS.between(t.atZone(zoneId), tFutur.atZone(zoneId));
    double valeurComptableFutur = valeurComptable + (valeurComptable * (nombreDAnnnee * tauxDAppreciationAnnuelle));
    return new Materiel(nom, tFutur, (int) valeurComptableFutur, tauxDAppreciationAnnuelle);
  }
}
