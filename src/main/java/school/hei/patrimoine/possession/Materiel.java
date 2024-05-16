package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    int DayBetweenTandTfutur = (int) Duration.between(this.t, tFutur).toDays();
    int TauxParJour = (int) Math.abs((tauxDAppreciationAnnuelle * valeurComptable /100) /365.5);
    int newValeurComptable = valeurComptable - (DayBetweenTandTfutur * TauxParJour);
    return new    Materiel(
            this.nom,
            tFutur,
            newValeurComptable,
            tauxDAppreciationAnnuelle
    );
  }
}
