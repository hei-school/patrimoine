package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.util.Set;
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

  @Override
  public Argent projectionFuture(Instant tFutur) {
    return new Argent(
        nom,
        tFutur,
        valeurComptable - financementsFutur(tFutur),
        financés.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
  }

  private int financementsFutur(Instant tFutur) {
    throw new NotImplemented();
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
