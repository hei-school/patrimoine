package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    int intervalInstant= ((tFutur.atZone(ZoneId.systemDefault()).getYear()) - t.atZone(ZoneId.systemDefault()).getYear());
    int valeurComptableAJour = (int) (valeurComptable + (intervalInstant * tauxDAppreciationAnnuelle));
    return  new Materiel(nom,t,valeurComptableAJour,tauxDAppreciationAnnuelle);
  }
}
