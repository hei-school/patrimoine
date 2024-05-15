package school.hei.patrimoine.possession;


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
    int nombreAnnees = ((tFutur.atZone(ZoneId.systemDefault()).getYear())-(t.atZone(ZoneId.systemDefault()).getYear()));
    int valeurComptables = (int) (valeurComptable + (nombreAnnees * tauxDAppreciationAnnuelle));
    Materiel materiel = new Materiel(nom,t,valeurComptables,tauxDAppreciationAnnuelle);
    return materiel;
  }
}
