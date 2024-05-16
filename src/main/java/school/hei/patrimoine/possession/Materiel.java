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
  public Materiel projectionFuture(Instant tFutur) {
    double joursEcoules = Duration.between(t, tFutur).toDays();
    double valeurAjouteeJournaliere = valeurComptable * (tauxDAppreciationAnnuelle / 365);
    int valeurComptableFuture = (int) (valeurComptable + (valeurAjouteeJournaliere * joursEcoules));
    return new Materiel(nom, tFutur, valeurComptableFuture, tauxDAppreciationAnnuelle);
  }
}
