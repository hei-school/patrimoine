package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_Zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);

        // Ordinateur
        int initialValueComputer = 1200000;
        double depreciationComputerAnnual = 0.10;
        double depreciationComputerDaily = Math.pow((1 - depreciationComputerAnnual), 1.0 / 365);
        int days = 76;
        int valueComputer17Sept2024 = (int) Math.round(initialValueComputer * Math.pow(depreciationComputerDaily, days));

        // Vêtements
        int initialValueClothes = 1500000;
        double depreciationClothesAnnual = 0.50;
        double depreciationClothesDaily = Math.pow((1 - depreciationClothesAnnual), 1.0 / 365);
        int valueClothes17Sept2024 = (int) Math.round(initialValueClothes * Math.pow(depreciationClothesDaily, days));

        // Argent en espèces
        int initialCash = 800000;
        int schoolFeesPerMonth = 200000;
        int paymentsRemaining = 3;
        int cashRemaining = initialCash - (schoolFeesPerMonth * paymentsRemaining);

        // Compte bancaire
        int initialBankAccount = 100000;
        int accountFeesPerMonth = 20000;
        int feesRemaining = 3;
        int bankAccountRemaining = initialBankAccount - (accountFeesPerMonth * feesRemaining);

        // Patrimoine
        var patrimoineZetyAu17sept24 = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au17sept24,
                Set.of(
                        new Argent("Ordinateur", au17sept24, valueComputer17Sept2024),
                        new Argent("Vêtements", au17sept24, valueClothes17Sept2024),
                        new Argent("Espèces", au17sept24, cashRemaining),
                        new Argent("Compte bancaire", au17sept24, bankAccountRemaining)));

        int expectedTotalValue = valueComputer17Sept2024 + valueClothes17Sept2024 + cashRemaining + bankAccountRemaining;
        assertEquals(expectedTotalValue, patrimoineZetyAu17sept24.getValeurComptable());
    }
}
