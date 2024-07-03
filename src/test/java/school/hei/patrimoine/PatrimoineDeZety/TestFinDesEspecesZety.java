package school.hei.patrimoine.PatrimoineDeZety;

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
    void zety_n_a_plus_especes_apres_18_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
        var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
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

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));

        LocalDate dateProjection = au3juillet2024;
        while (true) {
            if (dateProjection.equals(LocalDate.of(2024, Month.SEPTEMBER, 21))) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() - 2_500_000);
            }
            if (dateProjection.getDayOfMonth() == 15 && dateProjection.getMonthValue() >= 1 && dateProjection.getYear() == 2024) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() + 100_000);
            }
            if (dateProjection.getMonthValue() >= 10 && dateProjection.getDayOfMonth() == 1 &&
                    dateProjection.isBefore(LocalDate.of(2025, Month.FEBRUARY, 14))) {
                argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() - 250_000);
            }
            if (argentEspeces.getValeurComptable() <= 0) {
                break;
            }
            dateProjection = dateProjection.plusDays(1);
        }
        assertEquals(LocalDate.of(2024, Month.SEPTEMBER, 21), dateProjection);
    }
}