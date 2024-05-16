package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZonedDateTime;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  
  @Override
  public Possession projectionFuture(Instant tFutur) {
   ZonedDateTime dateFuture = ZonedDateTime.parse(tFutur.toString());
   ZonedDateTime datePassée = ZonedDateTime.parse(t.toString());
   int differenceYear = 0;
   differenceYear = dateFuture.getYear() - datePassée.getYear();

   Double gap = 1 + (tauxDAppreciationAnnuelle * differenceYear);
   int valeurComptableFuture = (int) (valeurComptable * gap);

   return new Materiel(
    getNom(),
    tFutur,
    valeurComptableFuture,
    tauxDAppreciationAnnuelle
   );
  }
}
