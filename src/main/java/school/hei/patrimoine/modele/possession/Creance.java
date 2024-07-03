package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

public final class Creance extends Argent {
  public Creance(String nom, LocalDate t, int valeurComptable) {
    super(nom, t, valeurComptable);
    if (valeurComptable < 0) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Creance projectionFuture(LocalDate tFutur) {
    return new Creance(nom, tFutur, valeurComptable);
  }
}
