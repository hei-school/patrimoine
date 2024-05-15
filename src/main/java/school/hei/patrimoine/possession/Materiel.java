package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {

    double ValeurAYearLater = valeurComptable * tauxDAppreciationAnnuelle;
    int YearBetweenTwoIinstant = (int) Duration.between(t,tFutur).toDays() / 365;
    int valeurComptableFuture = (int) (valeurComptable + (ValeurAYearLater * YearBetweenTwoIinstant));
    return new Materiel(this.nom,tFutur,valeurComptableFuture,this.tauxDAppreciationAnnuelle);
  }
}
