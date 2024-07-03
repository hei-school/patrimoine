/*package school.hei.patrimoine.PatrimoineDeZety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFinDesEspecesZety {

    @Test
    void dateFinDesEspecesZety() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024.minusDays(2),
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet2024,
                1_500_000,
                au3juillet2024.minusDays(2),
                -0.50);

        var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, Month.NOVEMBER, 27),
                LocalDate.of(2024, Month.AUGUST, 27),
                -200_000,
                30);

        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet2024.minusMonths(1),
                au3juillet2024.plusYears(1),
                -20_000,
                30);

        Set<Possession> possessions = new HashSet<>();
        possessions.add(ordinateur);
        possessions.add(vetements);
        possessions.add(argentEspeces);
        possessions.add(fraisScolarite);
        possessions.add(compteBancaire);
        possessions.add(fraisTenueCompte);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                possessions);

        // Zety paie ses frais de scolarité pour 2024-2025 le 21 septembre 2024
        var fraisScolarite2024_2025 = new Argent("Frais de scolarité 2024-2025", LocalDate.of(2024, Month.SEPTEMBER, 21), -2_500_000);

        // Ajout de frais de scolarité à partir de 21 septembre 2024
        patrimoineZety.ajouterPossession(fraisScolarite2024_2025);

        // Don mensuel des parents de Zety de 100 000 Ar à partir du 15 janvier 2024
        var donMensuelParents = new FluxArgent(
                "Don mensuel des parents",
                argentEspeces,
                LocalDate.of(2024, Month.JANUARY, 15),
                LocalDate.of(2025, Month.FEBRUARY, 13),
                100_000,
                15);

        // Ajout du don mensuel
        patrimoineZety.ajouterPossession(donMensuelParents);

        // Dépenses mensuelles de Zety de 250 000 Ar à partir du 1 octobre 2024
        var depensesMensuelles = new FluxArgent(
                "Dépenses mensuelles",
                argentEspeces,
                LocalDate.of(2024, Month.OCTOBER, 1),
                LocalDate.of(2025, Month.FEBRUARY, 13),
                -250_000,
                1);

        // Ajout des dépenses mensuelles
        patrimoineZety.ajouterPossession(depensesMensuelles);

        // Détermination de la date à partir de laquelle Zety n'a plus d'espèces
        LocalDate dateFinDesEspeces = patrimoineZety.dateEpuisementEspeces();

        assertEquals(LocalDate.of(2025, Month.FEBRUARY, 13), dateFinDesEspeces);
    }

}*/