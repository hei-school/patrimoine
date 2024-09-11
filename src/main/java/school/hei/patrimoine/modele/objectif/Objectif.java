package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;
import java.util.Optional;
import school.hei.patrimoine.modele.Argent;

public record Objectif(Objectivable objectivable, LocalDate t, Argent valeurComptable) {

  public Optional<ObjectifNonAtteint> verifier() {
    if (!objectivable.valeurAObjectifT(t).equals(valeurComptable)) {
      return Optional.of(new ObjectifNonAtteint(objectivable, this));
    }

    return Optional.empty();
  }
}
