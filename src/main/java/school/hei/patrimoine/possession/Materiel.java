package school.hei.patrimoine.possession;

import java.time.Instant;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
      Materiel materiel = new Materiel(nom, t, valeurComptable, tauxDAppreciationAnnuelle);
    int futur_valeur = 0;
  if (materiel.getValeurComptable() == materiel.valeurComptableFuture(tFutur)){
      futur_valeur = ( valeurComptable * 10) / 100;

  }
      return futur_valeur;
  }
}
