package school.hei.patrimoine.patrilang.modele;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public class PossessionAccumulator<T extends Possession> {
  private final Set<T> possessions;
  private final PossessionGetter<T> possessionGetter;

  public PossessionAccumulator() {
    this.possessions = new HashSet<>();
    this.possessionGetter = new PossessionGetter<>(this.possessions);
  }

  public Set<T> add(Set<T> possessions) {
    this.possessions.addAll(possessions);
    return possessions;
  }
}
