package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Devise;

public final class Dette extends Argent {

  public Dette(String nom, LocalDate t, int valeurComptable) {
    super(nom, t, valeurComptable);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }

  public Dette(String nom, LocalDate t, int valeurComptable, Devise devise) {
    super(nom, t, valeurComptable, devise);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }
}
