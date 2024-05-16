package school.hei.patrimoine.possession;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Argent extends Possession {
  private final Set<TrainDeVie> financés;

  public Argent(String nom, Instant t, int valeurComptable) {
    this(nom, t, valeurComptable, new HashSet<>());
  }

  private Argent(String nom, Instant t, int valeurComptable, Set<TrainDeVie> financés) {
    super(nom, t, valeurComptable);
    this.financés = financés;
  }

  public Argent projectionFuture(Instant tFutur) {
    Set<TrainDeVie> financéFuturs = new HashSet<>(financés);
    return new Argent(
        nom,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        financéFuturs.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(Instant tFutur) {
    Set<TrainDeVie> financiersFuturs = new HashSet<>(financés);
    return financiersFuturs.stream()
            .mapToInt(f -> f.valeurComptableFuture(tFutur))
            .sum();
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
