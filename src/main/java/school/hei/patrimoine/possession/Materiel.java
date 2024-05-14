package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.*;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    var differenceEntreTfuturEtT=t.until(tFutur,ChronoUnit.DAYS)/365;
    var futureValeurComptable=(double)valeurComptable;
    for(var i=1;i<=differenceEntreTfuturEtT;i++){
      futureValeurComptable*=(1+tauxDAppreciationAnnuelle);
    }
   return (int) futureValeurComptable;
  }



  }



