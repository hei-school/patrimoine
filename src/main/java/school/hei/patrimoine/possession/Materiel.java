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

  public double getTauxDAppreciationAnnuelle() {
    return tauxDAppreciationAnnuelle;
  }

  @Override
  public Materiel projectionFuture(Instant tFutur) {
    double tauxDAppreciationJournaliere = tauxDAppreciationAnnuelle/365;
    int jourDePossession =(int) Duration.between(t,tFutur).toDays();
    int valeurComptableFutur = (int) (valeurComptable*(1-tauxDAppreciationJournaliere*jourDePossession));
    return new Materiel(
            nom,
            tFutur,
            valeurComptableFutur,
            tauxDAppreciationAnnuelle
    );
  }
}
