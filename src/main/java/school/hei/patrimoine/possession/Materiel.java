package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

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
    ZonedDateTime zdt1 = this.t.atZone(ZoneId.systemDefault());
    ZonedDateTime zdt2 = tFutur.atZone(ZoneId.systemDefault());
    long nombreDeMois = ChronoUnit.MONTHS.between(zdt1, zdt2);
    double valeurActuelle = getValeurComptable();
    double tauxMensuel = tauxDAppreciationAnnuelle / 12.0;

    for (int mois = 1; mois <= nombreDeMois; mois++) {
      valeurActuelle *= (1 + tauxMensuel);
    }

    return (int) Math.round(valeurActuelle);
  }
}
