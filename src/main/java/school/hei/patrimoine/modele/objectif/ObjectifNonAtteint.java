package school.hei.patrimoine.modele.objectif;

public record ObjectifNonAtteint(Objectivable objectivable, Objectif objectif) {
  public String prettyPrint() {
    return String.format(
        "ObjectifNonAtteint[%s, t=%s, objectif=%s, atteint=%s]",
        objectivable.nom(),
        objectif.t(),
        objectif.valeurComptable(),
        objectivable.valeurAObjectifT(objectif.t()));
  }
}
