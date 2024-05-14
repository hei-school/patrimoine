package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    ZonedDateTime  dateFuture = ZonedDateTime.parse(tFutur.toString());
    ZonedDateTime  dateOld = ZonedDateTime.parse(t.toString());
    int differenceYear =0;
    Double gap = 0.10;
    differenceYear = dateFuture.getYear() - dateOld.getYear();
    gap *= differenceYear;
    return (int) (valeurComptable * gap);
  }
}
