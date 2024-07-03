package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static java.util.Calendar.*;
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
    void patrimoine_evolue_au_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000,
                30);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet24,
                au17sept24.plusDays(1),
                -20_000,
                30);

        var patrimoineZety = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "EvolutionPatrimoineZety",
                patrimoineZety,
                au3juillet24,
                au17sept24);

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        var valeurPatrimoineAu17sept24 = evolutionJournaliere.get(au17sept24).getValeurComptable();

        assertEquals(3_181_232, valeurPatrimoineAu17sept24);
    }

}