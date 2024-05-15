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
  public Possession projectionFuture(Instant tFutur) {
    long nombreDAnnnee = getNombreDAnneeEcoullee(tFutur);
    double croissance = Math.pow(1 + tauxDAppreciationAnnuelle, nombreDAnnnee);
    int valeurComptableFutur = (int) Math.round(valeurComptable * croissance);
    return new Materiel(nom, tFutur, valeurComptableFutur, tauxDAppreciationAnnuelle);
  }

  private long getNombreDAnneeEcoullee(Instant tFutur){
    ZoneId zoneId = ZoneId.systemDefault();
    return ChronoUnit.YEARS.between(t.atZone(zoneId), tFutur.atZone(zoneId));
  }
}
