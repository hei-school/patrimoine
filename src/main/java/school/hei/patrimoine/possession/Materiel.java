package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }
  public double getTauxDAppreciationAnnuelle() {
    return tauxDAppreciationAnnuelle;
  }



  @Override
  public Possession projectionFuture(Instant tFutur) {
    int future = tFutur.atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    int present = getT().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    int difference = future - present;
    double tauxappreciation = -0.10;
    double taux_annuelle = getTauxDAppreciationAnnuelle();
    for(int i = 0;i<=difference;i++){
      taux_annuelle += tauxDAppreciationAnnuelle;
    }
    double future_valeur_materiel = Double.valueOf(getValeurComptable()) - getTauxDAppreciationAnnuelle();
    int future_valeur_materiel_int = (int)future_valeur_materiel;
    var materiel = new Materiel(
            getNom(),
            getT(),
            future_valeur_materiel_int,
            getTauxDAppreciationAnnuelle()
    );
    return materiel;
  }
}
