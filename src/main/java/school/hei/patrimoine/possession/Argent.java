package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Getter
public final class Argent extends Possession {
  private final double tauxDinteret;

  public Argent(String nom, Instant t, int valeurComptable, double tauxDinteret) {
    super(nom, t, valeurComptable);
    this.tauxDinteret = tauxDinteret;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    if (tFutur.isBefore(this.t)) {
      return this.valeurComptable;
    }

    long joursEntre = ChronoUnit.DAYS.between(this.t, tFutur);
    double tauxInteretQuotidien = this.tauxDinteret / 365;

    double valeurFuture = this.valeurComptable * Math.pow(1 + tauxInteretQuotidien, joursEntre);

    return (int) Math.round(valeurFuture);
  }
}
