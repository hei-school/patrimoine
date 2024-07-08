package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public sealed class Argent extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;
  private final String devise;

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, String devise) {
    super(nom, t, valeurComptable);
    this.dateOuverture = dateOuverture;
    this.fluxArgents = new HashSet<>();
    this.devise = devise;
  }

  public Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, "Ar");
  }

  public Argent(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, t, valeurComptable, "Ar");
  }

  private Argent(String nom, LocalDate dateOuverture, LocalDate t, int valeurComptable, Set<FluxArgent> fluxArgents, String devise) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
      this.devise = devise;
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
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()), devise);
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

  @Override
  public int getValeurComptable(String deviseTarget, LocalDate date, Map<String, Double> tauxChange, Map<String, Double> tauxAppreciation) {
    if (devise.equals(deviseTarget)) {
      return valeurComptable;
    }
    return convertirValeur(valeurComptable, devise, deviseTarget, date, tauxChange, tauxAppreciation);
  }

  private int convertirValeur(int valeur, String deviseSource, String deviseTarget, LocalDate date, Map<String, Double> tauxChange, Map<String, Double> tauxAppreciation) {
    double tauxInitial = tauxChange.get(deviseSource + "-" + deviseTarget);
    double appreciation = tauxAppreciation.get(deviseSource + "-" + deviseTarget);
    long daysBetween = ChronoUnit.DAYS.between(t, date);
    double tauxFinal = tauxInitial * Math.pow(1 + appreciation, daysBetween / 365.0);
    return (int) (valeur * tauxFinal);
  }
}
