package school.hei.patrimoine.possession;


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
    double valeurAjouteeAnnuelle = (getValeurComptable() * tauxDAppreciationAnnuelle);
    long anneesDeDifference = Duration.between(t, tFutur).toDays() / 365;
    int valeurFuture = (int) (getValeurComptable() + (valeurAjouteeAnnuelle * anneesDeDifference));
    return new Materiel(this.nom, tFutur, valeurFuture, this.tauxDAppreciationAnnuelle);
  }
}
