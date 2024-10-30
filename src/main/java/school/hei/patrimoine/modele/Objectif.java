package school.hei.patrimoine.modele;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;

public record Objectif(LocalDate t, int valeurComptableMin, Integer valeurComptableMax) {
  public Objectif(LocalDate t, int valeurComptableMin) {
    this(t, valeurComptableMin, null);
  }

  @Getter
  public static class ObjectifNonAtteintException extends RuntimeException {
    private final Patrimoine patrimoine;
    private final Objectif objectif;

    public ObjectifNonAtteintException(Patrimoine patrimoine, Objectif objectif) {
      super(
          String.format(
              "patrimoine.nom=%s, objectif=%s, patrimoine.valeurComptable=%d",
              patrimoine.nom(),
              objectif,
              patrimoine.projectionFuture(objectif.t).getValeurComptable()));
      this.patrimoine = patrimoine;
      this.objectif = objectif;
    }
  }

  public static void verifierObjectifs(Patrimoine patrimoine, Set<Objectif> objectifs) {
    objectifs.forEach(
        objectif -> {
          var valeurComptableAObjectifT =
              patrimoine.projectionFuture(objectif.t()).getValeurComptable();
          var valeurComptableMax = objectif.valeurComptableMax();
          if (valeurComptableAObjectifT < objectif.valeurComptableMin()
              || valeurComptableMax != null && valeurComptableAObjectifT > valeurComptableMax) {
            throw new ObjectifNonAtteintException(patrimoine, objectif);
          }
        });
  }
}
