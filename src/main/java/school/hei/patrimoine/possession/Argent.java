package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

  @Override
  public Argent projectionFuture(Instant tFutur) {
    Argent projectArgent = new Argent(nom, tFutur, valeurComptable - financementsFutur(tFutur));
    financés.stream().map(t -> t.projectionFuture(tFutur, projectArgent));
    return projectArgent;
  }

  private int financementsFutur(Instant tFutur) {
    return (int) this.financés.stream()
                      .mapToLong(trainDeVie -> trainDeVie.financementsFutur(tFutur))
                      .sum();
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
