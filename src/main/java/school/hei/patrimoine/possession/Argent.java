package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    Instant instant = Instant.now();
    LocalDate datePresent = LocalDate.ofInstant(instant, ZoneId.systemDefault());
    LocalDate future= LocalDate.ofInstant(tFutur,ZoneId.systemDefault());
    int annee = future.getYear() - datePresent.getYear();
    int valeurFuture= (int) (valeurComptable*annee);
    return  new Argent(nom,t,valeurFuture);
  }
  }
}
