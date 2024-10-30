package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;

public record Objectif(LocalDate t, int valeurComptableMin, Integer valeurComptableMax) {
  public Objectif(LocalDate t, int valeurComptableMin) {
    this(t, valeurComptableMin, null);
  }

  @Getter
  public static class ObjectifNonAtteintException extends RuntimeException {
    private final Objectivable objectivable;
    private final Objectif objectif;

    public ObjectifNonAtteintException(Objectivable objectivable, Objectif objectif) {
      super(
          String.format(
              "objectivable.nom=%s, objectif=%s, objectivable.valeurComptable=%d",
              objectivable.nom(), objectif, objectivable.valeurAObjectifT(objectif.t)));
      this.objectivable = objectivable;
      this.objectif = objectif;
    }
  }

  public static void verifierObjectifs(Objectivable objectivable, Set<Objectif> objectifs) {
    objectifs.forEach(
        objectif -> {
          var valeurComptableAObjectifT = objectivable.valeurAObjectifT(objectif.t());
          var valeurComptableMax = objectif.valeurComptableMax();
          if (valeurComptableAObjectifT < objectif.valeurComptableMin()
              || valeurComptableMax != null && valeurComptableAObjectifT > valeurComptableMax) {
            throw new ObjectifNonAtteintException(objectivable, objectif);
          }
        });
  }
}
