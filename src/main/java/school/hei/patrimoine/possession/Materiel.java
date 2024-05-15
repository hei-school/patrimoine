package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Getter
public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }


  @Override
  public Possession projectionFuture(Instant tFutur) {
    Instant instant = Instant.now();

    LocalDate datePresent = LocalDate.ofInstant(instant, ZoneId.systemDefault());
    LocalDate future= LocalDate.ofInstant(tFutur,ZoneId.systemDefault());

    int annee = future.getYear() - datePresent.getYear();

    double diminution = 0;

    for (int i = 0; i < annee; i++) {
      diminution=diminution+tauxDAppreciationAnnuelle;
    }
    int valeurFuture= (int) (valeurComptable*diminution);
    return  new Materiel(nom,t,valeurFuture,tauxDAppreciationAnnuelle);
  }
}
