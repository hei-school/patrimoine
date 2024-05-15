package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Materiel projectionFuture(Instant tFutur) {
    float nombreDeJoursEntreTEtTFutur = ChronoUnit.DAYS.between(t, tFutur);
    int valeurComptableFuture = (int) (valeurComptable + (valeurComptable * (nombreDeJoursEntreTEtTFutur/365) * tauxDAppreciationAnnuelle));

    Materiel materielFutur = new Materiel(
            nom,
            tFutur,
            valeurComptableFuture,
            tauxDAppreciationAnnuelle
    );

    return materielFutur;
  }
}
