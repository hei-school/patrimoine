package school.hei.patrimoine.cas;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

@Slf4j
public abstract class Cas implements Supplier<Patrimoine> {
  @Getter protected final LocalDate ajd;
  @Getter protected final LocalDate finSimulation;

  protected final Supplier<Patrimoine> patrimoineSupplier;

  @Override
  public Patrimoine get() {
    // TODO: This is just for backward compatibility with Google Docs feat: eventually rm it
    return patrimoineSupplier.get();
  }

  @Getter protected final Set<Objectif> objectifs = new HashSet<>();

  protected Cas(LocalDate ajd, LocalDate finSimulation, Set<Personne> possesseurs) {
    this.ajd = ajd;
    this.finSimulation = finSimulation;
    this.patrimoineSupplier =
        // lazy init required as spec is declarative, not procedural
        () -> Patrimoine.of(nom(), devise(), ajd, possesseurs, possessions());
  }

  public Patrimoine patrimoine() {
    init();
    suivi();
    return patrimoineSupplier.get();
  }

  protected abstract Devise devise();

  protected abstract String nom();

  protected abstract void init();

  protected abstract void suivi();

  public Set<ObjectifNonAtteint> verifier() {
    init();
    suivi();
    warnPrecociousFluxArgent();
    return objectifs.stream().flatMap(objectif -> objectif.verifier().stream()).collect(toSet());
  }

  private void warnPrecociousFluxArgent() {
    patrimoine().getPossessions().stream()
        .filter(p -> p instanceof Compte)
        .flatMap(c -> ((Compte) c).getFluxArgents().stream())
        .forEach(
            p -> {
              if (p.getDebut().isBefore(ajd)) {
                log.warn("fluxArgent.debut before ajd found: {}", p);
              }
            });
  }

  public abstract Set<Possession> possessions();
}
