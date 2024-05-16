package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
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
    int totalDepense = 0;
    for (TrainDeVie trainDeVie : financés)
      totalDepense += trainDeVie.projectionFuture(tFutur).getFinancePar().getValeurComptable();
    return totalDepense;
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
