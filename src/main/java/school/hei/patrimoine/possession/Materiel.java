package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDateTime;
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
    int futurDate = LocalDateTime.ofInstant(tFutur, ZoneId.systemDefault()).getYear();
    int ancienneDate = LocalDateTime.ofInstant(t, ZoneId.systemDefault()).getYear();
    int differenceAnnee = futurDate - ancienneDate;
    int valeur = 0;
    for (int i=0; i<differenceAnnee; i++){
      valeur += (int) ((getValeurComptable()*this.tauxDAppreciationAnnuelle) / 100);
    }
    return getValeurComptable() + valeur;
  }
}
