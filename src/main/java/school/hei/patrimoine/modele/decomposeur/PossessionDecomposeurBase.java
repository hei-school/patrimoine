package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;

@RequiredArgsConstructor
public class PossessionDecomposeurBase<
        ToDecompose extends Possession, Decomposed extends Possession>
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
  @SuppressWarnings("all")
  public List<Decomposed> apply(ToDecompose toDecompose) {
    if (isOutOfRange(toDecompose)) {
      return List.of();
    }

    return List.of((Decomposed) toDecompose);
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
