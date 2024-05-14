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
  public int valeurComptableFuture(Instant tFutur) {
    ZoneId zoneId = ZoneId.systemDefault();
    long anneePassee = ChronoUnit.YEARS.between(t.atZone(zoneId), tFutur.atZone(zoneId));
    double valeurFuture = valeurComptable + (valeurComptable * tauxDAppreciationAnnuelle * anneePassee);
    return (int) valeurFuture;
  }
}
