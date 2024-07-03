package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EvolutionPatrimoineTest {

  @Test
  void patrimoine_evolue() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);
    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    var evolutionPatrimoine = new EvolutionPatrimoine(
        "Nom",
        patrimoineIloAu13mai24,
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
    assertEquals(0, evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
    assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
    assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
  }

  @Test
  void patrimoine_zety_17_Septembre() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2024, JULY, 25);

    var financeur = new Argent("Espèces", au3juillet24, 800_000);
    var financeur2 = new Argent("Compte bancaire", au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -10.0, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -50.0, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte)
    );

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, JULY, 3),
            LocalDate.of(2024, SEPTEMBER, 17)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    assertEquals(900_000, evolutionJournaliere.get(LocalDate.of(2024, JULY, 3)).getValeurComptable());
    assertEquals(-2_000_000, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable());
  }

  @Test
  void patrimoine_zety_18_Septembre() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au27Novembre2023 = LocalDate.of(2023, NOVEMBER, 27);
    var au27Aout2024 = LocalDate.of(2024, AUGUST, 27);
    var au25Juillet2024 = LocalDate.of(2024, JULY, 25);
    var auDateIndeterminee = LocalDate.of(2024, JULY, 25);
    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);


    var financeur = new Argent("Espèces", au3juillet24,800_000);
    var financeur2 = new Argent("Compte bancaire",au3juillet24, 100_000);

    var ordinateur = new AchatMaterielAuComptant("Ordinateur", au3juillet24, 1_200_000, -10.0, financeur);
    var vetement = new AchatMaterielAuComptant("Vêtements", au3juillet24, 1_500_000, -50.0, financeur);

    var fraisDeScolarite = new FluxArgent("Frais de scolarité", financeur, au27Novembre2023, au27Aout2024, -200_000, 30);
    var fraisTenueCompte = new FluxArgent("Frais tenue de compte", financeur2, au25Juillet2024, auDateIndeterminee, -20_000, 30);
    var loan = new FluxArgent("Prêt bancaire", financeur2, au18Septembre2024, au18Septembre2024, 10_000_000, 1);
    var debt = new Dette("Dette envers la banque", au18Septembre2024, -11_000_000);

    var patrimoineZetyAu3Juillet2024 = new Patrimoine(
            "patrimoineZety2024",
            zety,
            au3juillet24,
            Set.of(financeur, financeur2, ordinateur, vetement, fraisDeScolarite, fraisTenueCompte, loan, debt)
    );

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Zety",
            patrimoineZetyAu3Juillet2024,
            LocalDate.of(2024, SEPTEMBER, 17),
            LocalDate.of(2024, SEPTEMBER, 18)
    );

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    assertEquals(-2_000_000, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable());
    assertEquals(-13_000_000, evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 18)).getValeurComptable());
  }
}