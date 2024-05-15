package school.hei.patrimoine.possession;
import school.hei.patrimoine.NotImplemented;

import java.time.Duration;
import java.time.Instant;


public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  @Override
  public int valeurComptableFuture(Instant tFutur) {
    Duration intervalDuree = Duration.between(this.t, tFutur);
    long intervaleJours = intervalDuree.toDays();
    long intervalAnnee = intervaleJours / 365;
    double estimationTauxDAppreciation = Math.pow(1 + this.tauxDAppreciationAnnuelle, intervalAnnee);
    double valeurComptableFuture = this.valeurComptable * estimationTauxDAppreciation;
    return (int) Math.round(valeurComptableFuture);
    
  public Possession projectionFuture(Instant tFutur) {
      return new Materiel(this.nom,tFutur,valeurComptableFuture,this.tauxDAppreciationAnnuelle)
  }
}
