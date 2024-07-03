package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Monnaie;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class GroupePossession extends Possession {
  private final Set<Possession> possessions;

  public GroupePossession(String nom, LocalDate t, Set<Possession> possessions, Monnaie monnaie) {
    super(nom, t, possessions.stream().mapToInt(Possession::getValeurComptable).sum(), monnaie);
    this.possessions = possessions;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return new GroupePossession(
        nom, tFutur, possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()), monnaie.projectionFutur(tFutur));
  }
}
