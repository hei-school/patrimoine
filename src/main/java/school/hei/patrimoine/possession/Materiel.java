package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    long differenceAnnee = ChronoUnit.YEARS.between(t.atZone(ZoneId.systemDefault()), tFutur.atZone(ZoneId.systemDefault()));
    int valeurComptableFutur = (int) (valeurComptable + (differenceAnnee * tauxDAppreciationAnnuelle));

    return new Materiel(
            this.nom,
            tFutur,
            valeurComptableFutur,
            this.tauxDAppreciationAnnuelle
    );
  }
}
