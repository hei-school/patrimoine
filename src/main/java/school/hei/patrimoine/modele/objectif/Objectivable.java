package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;
import java.util.Set;

public interface Objectivable {
  String nom();

  int valeurAObjectifT(LocalDate t);

  default void verifier(Set<Objectif> objectifs) {
    objectifs.forEach(
        objectif -> {
          var valeurComptableAObjectifT = valeurAObjectifT(objectif.t());
          var valeurComptableMax = objectif.valeurComptableMax();
          if (valeurComptableAObjectifT < objectif.valeurComptableMin()
              || valeurComptableMax != null && valeurComptableAObjectifT > valeurComptableMax) {
            throw new ObjectifNonAtteintException(this, objectif);
          }
        });
  }
}
