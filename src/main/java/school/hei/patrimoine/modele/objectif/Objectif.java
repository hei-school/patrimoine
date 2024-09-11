package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;
import java.util.Optional;

public record Objectif(Objectivable objectivable, LocalDate t, int valeurComptable) {

  public Optional<ObjectifNonAtteint> verifier() {
    if (objectivable.valeurAObjectifT(t) != valeurComptable) {
      return Optional.of(new ObjectifNonAtteint(objectivable, this));
    }

    return Optional.empty();
  }
}
