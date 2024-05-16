package school.hei.patrimoine.possession;
import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  private static final int unAnneeEnJour = 365;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  @Override
  public Possession projectionFuture(Instant tFutur) {
     double tauxDAppParJour = this.getValeurComptable() * this.tauxDAppreciationAnnuelle / unAnneeEnJour;
     long intervalDeTemps = Duration.between(t, tFutur).toDays();
     int estimeTauxTotal = (int) (intervalDeTemps * tauxDAppParJour);
     int valeurComptableAuFuture = this.valeurComptable + estimeTauxTotal;
     return new Materiel(this.getNom(),tFutur, valeurComptableAuFuture, this.tauxDAppreciationAnnuelle);
  }
}
