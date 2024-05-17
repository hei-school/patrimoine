package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  public int valeurComptableFuture(Instant tFutur) {
    long monthsBetween = tFutur.toEpochMilli() - t.toEpochMilli();
    double depreciation = Math.pow(tauxDAppreciationAnnuelle, monthsBetween / (30.0 * 24 * 60 * 60 * 1000));

    return (int) (valeurComptable / depreciation);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    var annesEntre = ChronoUnit.YEARS.between(
            LocalDateTime.ofInstant(this.t, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC"))
    );
    var nouvelleValleurComptable = (int)(valeurComptable + (valeurComptable * tauxDAppreciationAnnuelle * (annesEntre+1))) ;
    return new Materiel(
            this.nom,
            tFutur,
            nouvelleValleurComptable,
            this.tauxDAppreciationAnnuelle
    );

  }
}
