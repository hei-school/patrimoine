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
  public Possession projectionFuture(Instant tFutur) {
      long nombre_de_jour = Duration.between(t, tFutur).toDays();
      int nombre_d_année = Math.toIntExact(nombre_de_jour / 365);
      int valeur = valeurComptable;
      for (int i = 0; i < nombre_d_année; i++) {
        valeur *= (1 + tauxDAppreciationAnnuelle);
      }
      return new Materiel(getNom(), tFutur, valeur, tauxDAppreciationAnnuelle);
  }
}
