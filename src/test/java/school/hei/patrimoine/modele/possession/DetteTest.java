package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DetteTest {
  @Test
  void Zety_s_endette(){
    Personne zety = new Personne("Zety");
    LocalDate dateAvant = LocalDate.of(2024, 9, 17);
    Patrimoine patrimoineAvant = new Patrimoine("Patrimoine de Zety", zety, dateAvant, Set.of());
    LocalDate dateApres = LocalDate.of(2024, 9, 18);
    Argent emprunt = new Argent("Emprunt", dateApres, 10000000);
    Dette dette = new Dette("Dette", dateApres, -11000000);
    Patrimoine patrimoineApres = new Patrimoine("Patrimoine de Zety", zety, dateApres, Set.of(emprunt, dette));
    int valeurAvant = patrimoineAvant.getValeurComptable();
    int valeurApres = patrimoineApres.getValeurComptable();
    int diminutionReelle = valeurApres - valeurAvant;
    assertEquals(-1_000_000, diminutionReelle);

  }
}