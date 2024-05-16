package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public final class Argent extends Possession {
  private final Set<TrainDeVie> financés;

  public Argent(String nom, Instant t, int valeurComptable) {
    this(nom, t, valeurComptable, Set.of());
  }

  private Argent(String nom, Instant t, int valeurComptable, Set<TrainDeVie> financés) {
    super(nom, t, valeurComptable);
    this.financés = financés;
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
  return new Argent(this.nom,tFutur,this.valeurComptable);
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
