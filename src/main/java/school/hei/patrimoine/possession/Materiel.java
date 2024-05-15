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

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    var yearsBetween = ChronoUnit.YEARS.between(LocalDateTime.ofInstant(this.t, ZoneId.of("UTC")), LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC")));
    return (int) (valeurComptable + valeurComptable * tauxDAppreciationAnnuelle * yearsBetween);
  }
}
