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
    int nombreDeMois = (int) ChronoUnit.MONTHS.between(dateDebut, dateFin);

    double facteurMensuel = 1 + (tauxDAppreciationAnnuelle / 12);
    double valeurFuture = getValeurComptable() * Math.pow(facteurMensuel, nombreDeMois);
    int valeurFinal = (int) Math.round(valeurFuture);

    return new Materiel(this.nom, tFutur, valeurFinal, this.tauxDAppreciationAnnuelle);
  }
}
