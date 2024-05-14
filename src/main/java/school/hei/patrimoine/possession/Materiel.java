package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.*;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
        Instant date = getT();

        LocalDate date_obtention = date.atZone(ZoneId.systemDefault()).toLocalDate();
        int annee_obtention = date_obtention.getYear();;

        LocalDate date_future = tFutur.atZone(ZoneId.systemDefault()).toLocalDate();
        int annee_futur = date_future.getYear();

        int difference_entre_anne_obtention_et_futur = annee_futur - annee_obtention;
        int reduction = 0;
        for(int i = 1; i <= difference_entre_anne_obtention_et_futur; i++){
          reduction += 10;
        }
        int valeur_reduite = (getValeurComptable() * reduction)/100;
        int valeur_finale = getValeurComptable() - valeur_reduite;
        return valeur_finale;
  }
}
