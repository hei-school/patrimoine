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
  public Possession projectionFuture(Instant tFutur) {
    ZonedDateTime zdt1 = this.t.atZone(ZoneId.systemDefault());
    ZonedDateTime zdt2 = tFutur.atZone(ZoneId.systemDefault());
    long nombreDeJours = ChronoUnit.DAYS.between(zdt1, zdt2);
    double valeurActuelle = getValeurComptable();
    double tauxJournalier = tauxDAppreciationAnnuelle / 365.0;

    for (int jour = 1; jour <= nombreDeJours; jour++) {
      valeurActuelle *= (1 + tauxJournalier);
    }
    return new Materiel(getNom(), tFutur, (int) valeurActuelle, tauxDAppreciationAnnuelle);
  }
}
