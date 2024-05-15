package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();
    double anneePassee = (double) ChronoUnit.DAYS.between(t.atZone(zoneId), tFutur.atZone(zoneId)) / 365;
    return new Argent(nom, tFutur, (int) (valeurComptable * anneePassee));
  }
}
