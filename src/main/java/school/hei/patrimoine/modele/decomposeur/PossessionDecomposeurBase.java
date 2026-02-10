package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public abstract class PossessionDecomposeurBase<ToDecompose extends Possession, Decomposed extends Possession>
    implements PossessionDecomposeur<ToDecompose, Decomposed> {
  private final LocalDate debut;
  private final LocalDate fin;

  protected LocalDate getT(ToDecompose possession) {
    return possession.t();
  }

  protected boolean isOutOfRange(ToDecompose possession) {
    return getT(possession).isBefore(getDebut()) || getT(possession).isAfter(getFin());
  }

  @Override
  public LocalDate getDebut() {
    return debut;
  }

  @Override
  public LocalDate getFin() {
    return fin;
  }
}
