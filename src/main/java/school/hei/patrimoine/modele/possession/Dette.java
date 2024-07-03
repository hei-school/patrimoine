package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.Currency;

public final class Dette extends Argent {
  public Dette(String nom, LocalDate t, int valeurComptable, Currency currency) {
    super(nom, t, valeurComptable, currency);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }
}
