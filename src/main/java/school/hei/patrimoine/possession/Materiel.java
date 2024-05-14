package school.hei.patrimoine.possession;


import java.time.Instant;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    Instant datePossession = getT();
    int anneeAcquisition = datePossession.atZone(ZoneId.systemDefault()).toLocalDate().getYear();

    int anneeFutur = tFutur.atZone(ZoneId.systemDefault()).toLocalDate().getYear();

    int differenceAnnees = anneeFutur - anneeAcquisition;
    int reduction = differenceAnnees * 10;

    int valeurReduite = (getValeurComptable() * reduction) / 100;
    return getValeurComptable() - valeurReduite;
  }
}
