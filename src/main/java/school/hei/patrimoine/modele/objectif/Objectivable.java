package school.hei.patrimoine.modele.objectif;

import java.time.LocalDate;

public interface Objectivable {
  String nom();

  int valeurAObjectifT(LocalDate t);
}
