package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class PossessionDecomposeurBase<T extends Possession> implements PossessionDecomposeur<T> {
  protected final LocalDate finSimulation;

  @Override
  public List<T> apply(T possession) {
    return List.of(possession);
  }
}
