package school.hei.patrimoine.modele.possession;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, t, valeurComptable);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>());
  }

  public Argent(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      int valeurComptable,
      Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.fluxArgents = new HashSet<>(fluxArgents);
    this.dateOuverture = dateOuverture;
  }

  @Override
  public Argent projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Argent(nom, tFutur, 0);
    }

    return new Argent(
        nom,
        dateOuverture,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(LocalDate tFutur) {
    return fluxArgents.stream()
        .mapToInt(
            f -> valeurComptable - f.projectionFuture(tFutur).getArgent().getValeurComptable())
        .sum();
  }

  public void addFinances(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }

  public LocalDate dateSansEspece() {
    LocalDate date = this.getT();
    int valeur = this.getValeurComptable();

    for (FluxArgent flux : this.fluxArgents) {
      while (date.isBefore(flux.getFin()) && valeur > 0) {
        valeur += flux.getFluxMensuel();
        date = date.plusMonths(1);
      }

      if (valeur <= 0) {
        return date;
      }
    }

    return null;
  }
}
