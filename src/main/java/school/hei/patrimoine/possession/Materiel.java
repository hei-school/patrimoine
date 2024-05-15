package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;
import java.time.*;
import java.time.temporal.ChronoUnit;


public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    ZonedDateTime StartIngDate = this.t.atZone(ZoneId.systemDefault());
    ZonedDateTime EndingDate = tFutur.atZone(ZoneId.systemDefault());
    int nombreDeMois = (int) ChronoUnit.MONTHS.between(StartIngDate, EndingDate);

    double MensualFactor = 1 + (tauxDAppreciationAnnuelle / 12);
    double valeurFuture = getValeurComptable() * Math.pow(MensualFactor, nombreDeMois);
    int valeurFinal = (int) Math.round(valeurFuture);

    return new Materiel(this.nom, tFutur, valeurFinal, this.tauxDAppreciationAnnuelle);
  }
  }
}
