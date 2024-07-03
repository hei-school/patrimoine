package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

public final class Dette extends Argent {
  private final double montantEUR;
  private final double tauxDeChangeEURtoAr;

  public Dette(String nom, LocalDate dateOuverture, LocalDate date, double montantEUR, double tauxDeChangeEURtoAr) {
    super(nom, dateOuverture, date, 0); // La dette est initialement à 0, le montant est géré par les flux
    this.montantEUR = montantEUR;
    this.tauxDeChangeEURtoAr = tauxDeChangeEURtoAr;
  }

  @Override
  public int getValeurComptableDevise(String devise) {
    if ("Ar".equals(devise)) {
      return (int) (montantEUR * tauxDeChangeEURtoAr);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
