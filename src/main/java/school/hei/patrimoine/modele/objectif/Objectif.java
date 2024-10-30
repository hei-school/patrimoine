package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;

public record Objectif(LocalDate t, int valeurComptableMin, Integer valeurComptableMax) {
  public Objectif(LocalDate t, int valeurComptableMin) {
    this(t, valeurComptableMin, null);
  }
}
