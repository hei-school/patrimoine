package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;
  private final Currency devise;

  public Argent(String nom, LocalDate t, int valeurComptable, Currency devise) {
    this(nom, t, t, valeurComptable, devise, new HashSet<>());
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Currency devise) {
    this(nom, dateOuverture, t, valeurComptable, devise, new HashSet<>());
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Currency devise, Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.dateOuverture = dateOuverture;
    this.fluxArgents = fluxArgents;
    this.devise = devise;
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
    return fluxArgents.stream()
            .mapToInt(f -> f.projectionFuture(tFutur).getValeurComptable())
            .sum();
  }

  void addFinancé(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }

  public Currency getDevise() {
    return devise;
  }
}
