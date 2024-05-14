package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    long nombreDannee = getNombreDanneeEntreDeuxInstants(this.t, tFutur);

    double appreciationFactor = 1.0 + (this.tauxDAppreciationAnnuelle * nombreDannee);

    return (int) (this.valeurComptable * appreciationFactor);
  }

  private long getNombreDanneeEntreDeuxInstants(Instant t, Instant tFutur) {
    LocalDateTime ancienDate = this.t.atZone(ZoneId.systemDefault()).toLocalDateTime();
    int ancienAnnee = ancienDate.getYear();

    LocalDateTime futurDate = tFutur.atZone(ZoneId.systemDefault()).toLocalDateTime();
    int futurAnnee = futurDate.getYear();

    return (int) (futurAnnee - ancienAnnee);
  }
}
