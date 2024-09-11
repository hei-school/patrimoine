package school.hei.patrimoine.modele;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class EvolutionPatrimoineTest {

  @Test
  void patrimoine_evolue() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Compte("Espèces", au13mai24, ariary(600_000));
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            15,
            ariary(-100_000));
    var patrimoineIloAu13mai24 =
        Patrimoine.of("patrimoineIloAu13mai24", MGA, au13mai24, ilo, Set.of(financeur, trainDeVie));

    var evolutionPatrimoine =
        new EvolutionPatrimoine(
            "Nom",
            patrimoineIloAu13mai24,
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2024, MAY, 17));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
    assertEquals(
        ariary(0), evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
    assertEquals(
        ariary(600_000),
        evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
    assertEquals(
        ariary(600_000),
        evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
    assertEquals(
        ariary(500_000),
        evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
    assertEquals(
        ariary(500_000),
        evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
    assertEquals(
        ariary(500_000),
        evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
  }
}
