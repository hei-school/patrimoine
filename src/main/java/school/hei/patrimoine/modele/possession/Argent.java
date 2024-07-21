package school.hei.patrimoine.modele.possession;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Devise;

@ToString
@Getter
public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Argent(String nom, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, t, t, valeurComptable, devise);
  }

  private Argent(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      int valeurComptable,
      Set<FluxArgent> fluxArgents,
      Devise devise) {
    super(nom, t, valeurComptable, devise);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  public Argent(
      String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>(), devise);
  }

  public Argent(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, t, valeurComptable);
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>());
  }

  private Argent(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      int valeurComptable,
      Set<FluxArgent> fluxArgents) {
    this(nom, dateOuverture, t, valeurComptable, fluxArgents, NON_NOMMEE);
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
        valeurComptable - financementsFuturs(tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()),
        devise);
  }

  private int financementsFuturs(LocalDate tFutur) {
    return fluxArgents.stream()
        .mapToInt(
            f -> valeurComptable - f.projectionFuture(tFutur).getArgent().getValeurComptable())
        .sum();
  }

  void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }
}
