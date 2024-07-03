package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

public final class Dette extends Argent {
  public Dette(String nom, LocalDate t, int valeurComptable) {
    super(nom, t, valeurComptable);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public Dette projectionFuture(LocalDate tFutur) {
    return new Dette(nom, tFutur, valeurComptable);
  }
}
