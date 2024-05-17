package school.hei.patrimoine.possession;

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

  public Possession projectionFuture(Instant tFutur) {
    long anneesEntre = ChronoUnit.YEARS.between(
            LocalDateTime.ofInstant(this.t, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC"))
    );
    int nouvelleValeurComptable = (int) (valeurComptable + (valeurComptable * tauxDAppreciationAnnuelle * (anneesEntre + 1)));
    return new Materiel(
            this.nom,
            tFutur,
            nouvelleValeurComptable,
            this.tauxDAppreciationAnnuelle
    );
  }
}
