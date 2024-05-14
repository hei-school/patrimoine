package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Getter
public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }
private static final double tauxDinteret = 0.04 / 365;
  @Override
  public int valeurComptableFuture(Instant tFutur) {
    if (tFutur.isBefore(this.t)) {
      return this.valeurComptable;
    }
    long joursEntre = ChronoUnit.DAYS.between(this.t, tFutur);
    double valeureFuture = this.getValeurComptable()  * Math.pow(1 + tauxDinteret, joursEntre);
    return (int) valeureFuture;
  }
}
