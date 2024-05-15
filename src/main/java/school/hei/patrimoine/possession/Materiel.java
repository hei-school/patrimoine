package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.*;


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
  
  
  public Possession projectionFuture(Instant tFutur) {
 Duration difference = Duration.between(t, tFutur);
    long differenceEnJours = difference.toDays();
    double differenceEnAnnees = differenceEnJours / 365;

    double nouvelleValeurComptable = getValeurComptable()* Math.pow(tauxDAppreciationAnnuelle,differenceEnAnnees);

    return new Materiel(getNom(), tFutur, (int) nouvelleValeurComptable, tauxDAppreciationAnnuelle);

  public Possession projectionFuture(Instant tFutur) {
    throw new NotImplemented();
  }
}
