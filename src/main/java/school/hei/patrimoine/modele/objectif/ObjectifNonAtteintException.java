package school.hei.patrimoine.modele.objectif;

import lombok.Getter;

@Getter
public class ObjectifNonAtteintException extends RuntimeException {
  private final Objectivable objectivable;
  private final Objectif objectif;

  public ObjectifNonAtteintException(Objectivable objectivable, Objectif objectif) {
    super(
        String.format(
            "objectivable.nom=%s, objectif=%s, objectivable.valeurComptable=%d",
            objectivable.nom(), objectif, objectivable.valeurAObjectifT(objectif.t())));
    this.objectivable = objectivable;
    this.objectif = objectif;
  }
}
