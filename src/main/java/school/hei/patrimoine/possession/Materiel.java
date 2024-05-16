package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;
  private final static short ANNEE = 365;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    double JourTaux = tauxDAppreciationAnnuelle / ANNEE;
    long joursEcoule = Duration.between(t, tFutur).toDays();
    double tauxTotal = Math.pow(1 + JourTaux, joursEcoule);
    int valeurComptableFutur = (int) (valeurComptable * tauxTotal);
    return new Materiel(nom, tFutur, valeurComptableFutur, tauxDAppreciationAnnuelle);
  }
  }

