package school.hei.patrimoine.modele.objectif;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectifExeption extends RuntimeException {
  private final Set<ObjectifNonAtteint> objectifNonAtteints;
}
