package school.hei.patrimoine.modele.calculation;

import java.time.LocalDate;
import java.util.Comparator;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.vente.ValeurMarche;

public class ValeurMarcheHistorique implements ValeurMarcheCase {
  @Override
  public Argent calculateValeurCase(Possession possession, LocalDate date) {
    return possession.historiqueValeurMarche().stream()
        .filter(valeurMarche -> !valeurMarche.t().isAfter(date))
        .max(Comparator.comparing(ValeurMarche::t))
        .map(ValeurMarche::valeur)
        .orElse(possession.valeurComptable());
  }
}
