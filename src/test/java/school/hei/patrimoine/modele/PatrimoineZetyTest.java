package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatrimoineZetyTest {

    @Test
    void patrimoine_zety_evaluation() {
        var zety = new Personne("Zety");
        var au03jul2024 = LocalDate.of(2024, JULY, 3);

        var ordi = new Materiel(
                "Ordinateur",
                au03jul2024,
                1_200_000,
                au03jul2024.minusDays(1),
                -0.10);
        var vetments = new Materiel(
                "Vetments",
                au03jul2024,
                1_500_000,
                au03jul2024.minusDays(30),
                -0.50);

        var argentEspeces = new Argent("Especes", au03jul2024, 800_000);
        var debutAnneeScolaire = LocalDate.of(2023, NOVEMBER, 1);
        var finAnneeScolaire =  LocalDate.of(2024, AUGUST, 30);
        var fraisDeScolarite = new FluxArgent(
                "Frais de scolarité pour 2023-2024",
                argentEspeces, debutAnneeScolaire, finAnneeScolaire, -200_000,
                27);

        var compteBancaire = new Argent("Compte bancaire", au03jul2024, 100_000);
        var fraisDeTenue = new FluxArgent(
                "Frais de tenue du compte bancaire",
                compteBancaire, au03jul2024, LocalDate.of(2054, JULY, 1), -20_000,
                25);

        var patrimoineZetyAu3jul2024 = new Patrimoine(
                "patrimoineZetyAu3jul2024",
                zety,
                au03jul2024,
                Set.of(new GroupePossession("Le groupe", au03jul2024, Set.of(ordi, vetments, argentEspeces, compteBancaire))));

        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        assertTrue(patrimoineZetyAu3jul2024.projectionFuture(au17Sept2024).getValeurComptable() < 3_600_000);
        assertEquals(2_978_848, patrimoineZetyAu3jul2024.projectionFuture(au17Sept2024).getValeurComptable());
    }
}
