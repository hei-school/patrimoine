package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

public final class Dette extends Argent {

  public Dette(String nom, LocalDate t, int valeurComptable, Devise devise) {
    super(nom, t, valeurComptable, devise);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }

  public Dette(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, valeurComptable, NON_NOMMEE);
  }
}
