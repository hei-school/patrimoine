package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public interface Objectivable {
  String nom();

  Argent valeurAObjectifT(LocalDate t);
}
