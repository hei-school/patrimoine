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
    long nombreDeJourJusquaTFutur = Duration.between(this.t, tFutur).toDays();
    double tauxDAppreciationJournalier = this.tauxDAppreciationAnnuelle / 365;
    double valeur = this.valeurComptable;
    for (int jour = 0; jour < nombreDeJourJusquaTFutur; jour++) {
      valeur += valeur * tauxDAppreciationJournalier;
    }

    return new Materiel(this.nom, tFutur, (int) valeur, this.tauxDAppreciationAnnuelle);
  }
}
