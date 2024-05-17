package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Argent extends Possession {
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, Instant t, int valeurComptable) {
    this(nom, t, valeurComptable, new HashSet<>());
  }

  private Argent(String nom, Instant t, int valeurComptable, Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
  }

  @Override
  public Argent projectionFuture(Instant tFutur) {
    return new Argent(
        nom,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(Instant tFutur) {
    throw new NotImplemented();
  }

  void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }
}
