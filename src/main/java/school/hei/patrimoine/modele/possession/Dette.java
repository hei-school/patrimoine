package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

public final class Dette extends Argent {
  private final int coutDuPret;

  public Dette(String nom, LocalDate t, int valeurComptable, int coutDuPret) {
    super(nom, t, valeurComptable);
    if (valeurComptable < 0) {
      throw new IllegalArgumentException();
    }
    this.coutDuPret = coutDuPret;
  }

  public int getCoutDuPret() {
    return coutDuPret;
  }

  @Override
  public int getValeurComptable() {
    return super.getValeurComptable() - coutDuPret;
  }
}