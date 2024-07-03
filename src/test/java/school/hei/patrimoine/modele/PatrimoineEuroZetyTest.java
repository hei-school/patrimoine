package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineEuroZetyTest {

    @Test
    void valeur_patrimoine_zety_le_26_octobre_2025_en_euros() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, Month.JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.0996);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.4988);

        var euro = Currency.getInstance("EUR");
        var ariary = Currency.getInstance("MGA");

        var espece = new Argent("Espèces", au3juillet24, 800_000, ariary);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000, euro);
        var fraisTenueCompte = new FluxArgent(
                "Frais tenue de compte",
                compteBancaire, au3juillet24, LocalDate.of(2024, Month.SEPTEMBER, 25), -20_000, 25);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                espece, LocalDate.of(2023, Month.NOVEMBER, 27), LocalDate.of(2024, Month.AUGUST, 27), -200_000, 30);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite));

        LocalDate currentDate = au3juillet24.plusMonths(1);
        while (currentDate.isBefore(LocalDate.of(2025, Month.OCTOBER, 26))) {
            currentDate = currentDate.plusMonths(1);
        }

        var dette = new Dette("Dette en €", LocalDate.of(2025, Month.FEBRUARY, 15), -7000, euro);
        patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                LocalDate.of(2025, Month.FEBRUARY, 15),
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite, dette));

        double tauxChangeInitial = 4821.0;
        double appreciationAnnuelle = -0.10;

        int valeurComptableEnEuros = patrimoineZety.getValeurComptableEnDevise("EUR", tauxChangeInitial, appreciationAnnuelle);

        int valeurAttendueEnEuros = 0;

        assertEquals(valeurAttendueEnEuros, valeurComptableEnEuros);
    }
}
