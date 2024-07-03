package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

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
  void patrimoine_de_zety_apres_dette(){
    var zety = new Personne("Zety");
    var au3Juillet2024 = LocalDate.of(2024, JULY, 3);
    var au17Septembre2024 = LocalDate.of(20224, SEPTEMBER, 17);
    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);

    var ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024, -0.10);
    var vêtements = new Materiel("vêtements", au3Juillet2024, 1_500_000, au3Juillet2024, -0.50);

    var argentEnEspèces = new Argent("Espèces", au3Juillet2024, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au3Juillet2024, 100_000);
    var fraisDeScolarité = new FluxArgent("Frais de scolarité", argentEnEspèces, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
    var fraisCompteBancaire = new FluxArgent("frais compte bancaire", compteBancaire, au3Juillet2024, au17Septembre2024, -20_000, 25);

    var dette = new Dette("dette", au18Septembre2024, -11_000_000);

    var patrimoineDeZetyAu3Juillet2024 = new Patrimoine("patrimoine de zety au 3 juillet 2024",
            zety,
            au3Juillet2024,
            Set.of(ordinateur, vêtements, argentEnEspèces, compteBancaire, fraisDeScolarité, fraisCompteBancaire, dette));

    assertEquals(11002384, patrimoineDeZetyAu3Juillet2024.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() - patrimoineDeZetyAu3Juillet2024.projectionFuture(au18Septembre2024).getValeurComptable());
  }

  @Test
  void espèce_remise_à_0(){
    var zety = new Personne("Zety");
    var au3Juillet2024 = LocalDate.of(2024, JULY, 3);
    var au17Septembre2024 = LocalDate.of(20224, SEPTEMBER, 17);
    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
    var au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);

    var ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024, -0.10);
    var vêtements = new Materiel("vêtements", au3Juillet2024, 1_500_000, au3Juillet2024, -0.50);

    var argentEnEspèces = new Argent("Espèces", au3Juillet2024, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au3Juillet2024, 100_000);
    var fraisDeScolarité = new FluxArgent("Frais de scolarité", argentEnEspèces, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
    var fraisCompteBancaire = new FluxArgent("frais compte bancaire", compteBancaire, au3Juillet2024, au17Septembre2024, -20_000, 25);
    var dette = new Dette("dette", au18Septembre2024, -11_000_000);
    var fraisAnnuel = new FluxArgent("frais annuel", compteBancaire, au21Septembre2024, au21Septembre2024, 2_500_000, 21);
    var don = new FluxArgent("don", compteBancaire, LocalDate.of(2024, JANUARY, 1), LocalDate.of(2025, DECEMBER, 12), 100_000, 15);
    var trainDeVie = new FluxArgent("train de vie", argentEnEspèces, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2024, FEBRUARY, 13), 250_000, 1);

    var patrimoineDeZety = new Patrimoine("patrimoine de zety au 3 juillet 2024",
            zety,
            au3Juillet2024,
            Set.of(ordinateur, vêtements, argentEnEspèces, compteBancaire, fraisDeScolarité, fraisCompteBancaire, dette, fraisAnnuel, trainDeVie, don));
    assertEquals(0, argentEnEspèces.projectionFuture(LocalDate.of(2024, JANUARY, 1)).getValeurComptable());
  }
}