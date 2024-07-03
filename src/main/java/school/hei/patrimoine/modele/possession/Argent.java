package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, t, valeurComptable, devise);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>(), devise);
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents, Devise devise) {
    super(nom, t, valeurComptable, devise);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur, Devise devise) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Argent(nom, tFutur, 0, devise);
    }

    return new Argent(
        nom,
        dateOuverture,
        tFutur,
        valeurComptable - financementsFutur(tFutur, devise),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur, devise)).collect(toSet()), devise);
  }

  private int financementsFutur(LocalDate tFutur, Devise devise) {
    return fluxArgents.stream().
        mapToInt(
            f -> valeurComptable - f.projectionFuture(tFutur, devise).getArgent().getValeurComptable())
        .sum();
  }

  void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }
}
