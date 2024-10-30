package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;

public record Objectif(
    Objectivable objectivable, LocalDate t, int valeurComptableMin, Integer valeurComptableMax) {
  public Objectif(Objectivable objectivable, LocalDate t, int valeurComptableMin) {
    this(objectivable, t, valeurComptableMin, null);
  }

  public void verifier() {
    var valeurComptableAObjectifT = objectivable.valeurAObjectifT(t);
    if (valeurComptableAObjectifT < valeurComptableMin()
        || valeurComptableMax != null && valeurComptableAObjectifT > valeurComptableMax) {
      throw new ObjectifNonAtteintException(objectivable, this);
    }
  }
}
