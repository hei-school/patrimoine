package school.hei.patrimoine.modele.objectif;

public record ObjectifNonAtteint(Objectivable objectivable, Objectif objectif) {
  public String prettyPrint() {
    var aAtteindre = objectif.valeurComptable();
    return String.format(
        "ObjectifNonAtteint[%s, t=%s, devise=%s, objectif=%s, atteint=%s]",
        objectivable.nom(),
        objectif.t(),
        aAtteindre.devise().symbole(),
        aAtteindre.ppMontant(),
        objectivable.valeurAObjectifT(objectif.t()).ppMontant());
  }
}
