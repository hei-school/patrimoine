package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

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

  private int financeurFutur(Instant tFutur){
    return financés.stream().mapToInt(f->this.valeurComptable - f.projectionFuture(tFutur).getFinancePar().getValeurComptable()).sum();
  }

  @Override
  public Argent projectionFuture(Instant tFutur) {
    Set<TrainDeVie> nouvelleFinance = new HashSet<>();
    for (TrainDeVie trainDeVie:financés){
      if (!trainDeVie.getDebut().isAfter(tFutur) &&
      !trainDeVie.getFin().isBefore(tFutur)){
        nouvelleFinance.add(trainDeVie.projectionFuture(tFutur));
      }
    }
    return new Argent(
            nom,
            tFutur,
            valeurComptable - financeurFutur(tFutur),
            nouvelleFinance);
  }

  void addFinancés(TrainDeVie trainDeVie) {
    financés.add(trainDeVie);
  }
}
