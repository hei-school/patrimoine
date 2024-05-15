package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

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
  public Possession projectionFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();
    double anneePassee = (double) ChronoUnit.DAYS.between(t.atZone(zoneId), tFutur.atZone(zoneId)) / 365;
    double valeurFuture = valeurComptable + (valeurComptable * tauxDAppreciationAnnuelle * anneePassee);
    return new Materiel(nom, tFutur, (int) valeurFuture, tauxDAppreciationAnnuelle);
  }
}
