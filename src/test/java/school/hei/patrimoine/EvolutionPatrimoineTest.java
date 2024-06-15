package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

  @Test
  void patrimoine_evolue() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        -100_000,
        au13mai24.minus(100, DAYS),
        au13mai24.plus(100, DAYS),
        financeur, 15);
    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    var evolutionPatrimoine = new EvolutionPatrimoine(
        "Nom",
        patrimoineIloAu13mai24,
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
  }
}