package school.hei.patrimoine.possession;

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
    ZonedDateTime dateDebut = this.t.atZone(ZoneId.systemDefault());
    ZonedDateTime dateFin = tFutur.atZone(ZoneId.systemDefault());
    int nombreDeJour = (int) ChronoUnit.DAYS.between(dateDebut, dateFin);

    double facteurJournalier = 1 + (tauxDAppreciationAnnuelle / 365.2524);
    double valeurFuture = getValeurComptable() * Math.pow(facteurJournalier, nombreDeJour);
    int valeurFinal = (int) Math.round(valeurFuture);

    return new Materiel(this.nom, tFutur, valeurFinal, this.tauxDAppreciationAnnuelle);
  }
}
