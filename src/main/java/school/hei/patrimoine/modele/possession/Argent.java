package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, t,devise, valeurComptable);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, Devise devise, int valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, devise, new HashSet<>());
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Devise devise, Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable, devise);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Argent(nom, tFutur, 0, devise);
    }

    return new Argent(
        nom,
        dateOuverture,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        devise,
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
