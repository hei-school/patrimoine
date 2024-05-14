package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    double nombreDAnnnee = (double) getNombreDeMoisEcoullee(tFutur) / 12;
    double croissance = Math.pow(1 + this.tauxDAppreciationAnnuelle, nombreDAnnnee);
    return (int) Math.round(this.valeurComptable * croissance);
  }

  private long getNombreDeMoisEcoullee(Instant tFutur){
    ZonedDateTime startTime = this.t.atZone(ZoneId.systemDefault());
    ZonedDateTime endTime = tFutur.atZone(ZoneId.systemDefault());
    return ChronoUnit.MONTHS.between(startTime, endTime);
  }
}
