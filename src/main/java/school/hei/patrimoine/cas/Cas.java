package school.hei.patrimoine.cas;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.modele.possession.Possession;

public abstract class Cas {
  protected final LocalDate ajd;
  protected final LocalDate finSimulation;

  protected final Supplier<Patrimoine> patrimoineSupplier;

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
    return objectifs.stream().flatMap(objectif -> objectif.verifier().stream()).collect(toSet());
  }

  public abstract Set<Possession> possessions();
}
