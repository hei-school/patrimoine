package school.hei.patrimoine.modele.possession;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;
import static school.hei.patrimoine.modele.possession.TypeAgregat.TRESORIE;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.Personne;

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
      Devise devise,
      Set<FluxArgent> fluxArgents,
      Map<Personne, Double> possesseurs) {
    super(nom, t, valeurComptable, devise, possesseurs);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  public Argent(
      String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Devise devise) {
    this(nom, dateOuverture, t, valeurComptable, devise, new HashSet<>(), Map.of());
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
    this(nom, dateOuverture, t, valeurComptable, NON_NOMMEE, fluxArgents, Map.of());
  }

  private Argent(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      int valeurComptable,
      Set<FluxArgent> fluxArgents,
      Map<Personne, Double> possesseurs) {
    this(nom, dateOuverture, t, valeurComptable, NON_NOMMEE, fluxArgents, possesseurs);
  }

  private Argent(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      int valeurComptable,
      Devise devise,
      Set<FluxArgent> fluxArgents) {
    this(nom, dateOuverture, t, valeurComptable, devise, fluxArgents, Map.of());
  }

  public Argent(String nom, LocalDate t, int valeurComptable, Map<Personne, Double> possesseurs) {
    this(nom, t, t, valeurComptable, NON_NOMMEE, new HashSet<>(), possesseurs);
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
        devise,
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  @Override
  public TypeAgregat typeAgregat() {
    return TRESORIE;
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
