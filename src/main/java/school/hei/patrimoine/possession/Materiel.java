package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
   ZonedDateTime anneeFutur = tFutur.atZone(ZoneId.systemDefault());
   ZonedDateTime anneePresent = t.atZone(ZoneId.systemDefault());

   int moyenneAnnee = anneeFutur.getYear() - anneePresent.getYear();

   return (int) (valeurComptable*(moyenneAnnee*tauxDAppreciationAnnuelle));
}
  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
