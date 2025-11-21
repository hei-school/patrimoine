package school.hei.patrimoine.modele.objectif;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ObjectifExeption extends RuntimeException {
  private final Set<ObjectifNonAtteint> objectifNonAtteints;

  public ObjectifExeption(Set<ObjectifNonAtteint> objectifNonAtteints) {
    super("Les objectifs suivants n'ont pas été atteints : " + objectifNonAtteints);
    this.objectifNonAtteints = objectifNonAtteints;
  }
}
