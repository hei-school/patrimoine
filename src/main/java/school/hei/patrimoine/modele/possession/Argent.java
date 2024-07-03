package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Monnaie;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable, Monnaie monnaie) {
    this(nom, t, t, valeurComptable, monnaie);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Monnaie monnaie) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>(), monnaie);
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents, Monnaie monnaie) {
    super(nom, t, valeurComptable, monnaie);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Argent(nom, tFutur, 0, monnaie.projectionFutur(tFutur));
    }

    return new Argent(
        nom,
        dateOuverture,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()), monnaie.projectionFutur(tFutur));
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
