package school.hei.patrimoine.possession;

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
    long differenceDeJour = ChronoUnit.DAYS.between(getT(), tFutur);
    double differenceDAnnee = differenceDeJour / 365.25;
    double valeurComptableFutur = getValeurComptable() + getValeurComptable() * (tauxDAppreciationAnnuelle * differenceDAnnee);
    return new Materiel(getNom(), tFutur, (int) valeurComptableFutur, tauxDAppreciationAnnuelle);
  }
}
