package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Argent extends Possession {
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, valeurComptable, new HashSet<>());
  }

  private Argent(String nom, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    return new Argent(
        nom,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(LocalDate tFutur) {
    return fluxArgents.stream().
        mapToInt(
            f -> valeurComptable - f.projectionFuture(tFutur).getArgent().getValeurComptable())
        .sum();
  }

  void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }
}
