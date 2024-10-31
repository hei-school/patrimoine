package school.hei.patrimoine.cas;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Possession;

@AllArgsConstructor
public abstract class Cas implements Supplier<Patrimoine> {
  protected final LocalDate ajd;
  protected final LocalDate finSimulation;

  protected final Set<Personne> possesseurs;

  protected final Set<Objectif> objectifs = new HashSet<>();

  protected abstract String nom();

  protected abstract void init();

  protected abstract void suivi();

  protected abstract Supplier<Set<Possession>> possessionsSupplier();

  @Override
  public final Patrimoine get() {
    init();
    suivi();
    return Patrimoine.of(nom(), possesseurs, ajd, possessionsSupplier().get());
  }
}
