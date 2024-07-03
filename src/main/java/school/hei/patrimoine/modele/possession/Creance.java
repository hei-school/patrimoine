package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Monnaie;

import java.time.LocalDate;

public final class Creance extends Argent {
  public Creance(String nom, LocalDate t, int valeurComptable, Monnaie monnaie) {
    super(nom, t, valeurComptable, monnaie);
    if (valeurComptable < 0) {
      throw new IllegalArgumentException();
    }
  }
}
