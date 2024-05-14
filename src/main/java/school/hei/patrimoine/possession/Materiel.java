package school.hei.patrimoine.possession;


import java.time.*;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    int anneeDEnregistrement = LocalDateTime.ofInstant(getT(), ZoneId.systemDefault()).getYear();
    int anneeFuture = LocalDateTime.ofInstant(tFutur, ZoneId.systemDefault()).getYear();
    int valeurAReduire = 0;
    int anneeDeDifference = anneeFuture - anneeDEnregistrement;
    for (int i = 0; i < anneeDeDifference ; i++) {
      valeurAReduire += (int) ((getValeurComptable() * this.tauxDAppreciationAnnuelle) / 100);
    }
    return getValeurComptable() + valeurAReduire;
  }
}
