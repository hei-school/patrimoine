package school.hei.patrimoine.modele.possession;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import school.hei.patrimoine.modele.Devise;

@Getter
public final class GroupePossession extends Possession {

  private final Set<Possession> possessions;

  public GroupePossession(String nom, LocalDate t, Set<Possession> possessions, Devise devise) {
    super(nom, t, possessions.stream().mapToInt(Possession::getValeurComptable).sum(), devise);
    this.possessions = possessions;
  }

  public GroupePossession(String nom, LocalDate t, Set<Possession> possessions) {
    this(nom, t, possessions, NON_NOMMEE);
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return new GroupePossession(
        nom,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()),
        devise);
  }
}
