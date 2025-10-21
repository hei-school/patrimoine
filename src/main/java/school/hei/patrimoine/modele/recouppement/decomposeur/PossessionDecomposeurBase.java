package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PossessionDecomposeurBase<ToDecompose, Decomposed>
    implements PossessionDecomposeur<ToDecompose, Decomposed> {
  private final LocalDate finSimulation;

  @Override
  public LocalDate getFinSimulation() {
    return finSimulation;
  }
}
