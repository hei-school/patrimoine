package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.Devise.NON_NOMMEE;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Devise;

public final class Dette extends Argent {

  public Dette(String nom, LocalDate t, int valeurComptable, Devise devise) {
    super(nom, t, valeurComptable, devise);
    if (valeurComptable > 0) {
      throw new IllegalArgumentException();
    }
  }

  public Dette(String nom, LocalDate t, int valeurComptable) {
    this(nom, t, valeurComptable, NON_NOMMEE);
  }

  private Dette(Argent argent) {
    this(argent.nom, argent.t, argent.valeurComptable, argent.devise);
  }

  @Override
  public Dette projectionFuture(LocalDate tFutur) {
    return new Dette(super.projectionFuture(tFutur));
  }
}
