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
  public Materiel projectionFuture(Instant dateFuture) {
    long joursEntre = Duration.between(getT(), dateFuture).toDays();
    double anneesEntre = joursEntre / 365.0;

    double valeurProjete = getValeurComptable() * Math.pow(1 + tauxDAppreciationAnnuelle, anneesEntre);

    return new Materiel(
            getNom(),
            dateFuture,
            (int) valeurProjete,
            tauxDAppreciationAnnuelle
    );
  }
}
