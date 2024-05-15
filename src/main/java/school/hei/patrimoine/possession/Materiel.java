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
    Duration duree = Duration.between(getT(), tFutur);
    int nombre_d_annees = (int) (duree.toDays() / 365);
    int valeurComptableFuture = (int) (getValeurComptable() * tauxDAppreciationAnnuelle * nombre_d_annees);
    return new Materiel(getNom(), tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}