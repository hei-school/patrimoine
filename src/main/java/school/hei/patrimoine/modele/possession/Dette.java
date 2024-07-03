package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

public final class Dette extends Argent{
  private final String devise;

  public Dette(String nom, LocalDate t, int valeurComptable, String devise) {
    super(nom, t, valeurComptable);
    this.devise = devise;

    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }
  @Override
  public int getValeurComptable() {
    return super.getValeurComptable();
  }

  @Override
  public Possession convertirEnDevise(String nouvelleDevise) {
    // Ici vous pourriez implémenter la logique de conversion si nécessaire
    return this;
  }

  public String getDevise() {
    return devise;
  }
}
