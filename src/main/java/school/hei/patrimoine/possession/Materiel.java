package school.hei.patrimoine.possession;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class Materiel extends Possession {
  private final double tauxDAppreciationAnnuelle;

  public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public int valeurComptableFuture(Instant tFutur) {
    LocalDate datePossession = getT().atZone(ZoneId.systemDefault()).toLocalDate();
    int anneeAcquisition = datePossession.getYear();

    LocalDate dateFutur = tFutur.atZone(ZoneId.systemDefault()).toLocalDate();
    int anneeFutur = dateFutur.getYear();

    int differenceAnnees = anneeFutur - anneeAcquisition;
    int reduction = differenceAnnees * 10;

    int valeurReduite = (getValeurComptable() * reduction) / 100;
    return getValeurComptable() - valeurReduite;
  }
}
