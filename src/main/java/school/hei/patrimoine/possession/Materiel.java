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
  public int valeurComptableFuture(Instant tFutur) {
    Duration duree = Duration.between(t, tFutur);
    int nombre_d_annees = (int) (duree.toDays() / 365);
    int valeurComptableFuture = (int) (getValeurComptable() * tauxDAppreciationAnnuelle * nombre_d_annees);
    return valeurComptableFuture;
  }
}
