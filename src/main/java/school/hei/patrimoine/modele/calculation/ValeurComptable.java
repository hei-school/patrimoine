package school.hei.patrimoine.modele.calculation;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public class ValeurComptable implements ValeurMarcheCase {

  @Override
  public Argent calculateValeurCase(Possession possession, LocalDate date) {
    return possession.valeurComptable();
  }
}
