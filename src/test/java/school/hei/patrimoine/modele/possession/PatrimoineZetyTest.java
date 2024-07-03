package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.EvolutionPatrimoine;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {

    @Test
    void patrimoine_de_Zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");

        var au3Juillet24 = LocalDate.of(2024, JULY, 3);
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);

        // Calcul des valeurs individuelles
        int initialValueComputer = 1200000;
        double depreciationComputerAnnual = 0.10;
        double depreciationComputerDaily = Math.pow((1 - depreciationComputerAnnual), 1.0 / 365);
        int days = 76;
        int valueComputer17Sept2024 = (int) Math.round(initialValueComputer * Math.pow(depreciationComputerDaily, days));

        int initialValueClothes = 1500000;
        double depreciationClothesAnnual = 0.50;
        double depreciationClothesDaily = Math.pow((1 - depreciationClothesAnnual), 1.0 / 365);
        int valueClothes17Sept2024 = (int) Math.round(initialValueClothes * Math.pow(depreciationClothesDaily, days));

        int initialCash = 800000;
        int schoolFeesPerMonth = 200000;
        int paymentsRemaining = 3;
        int cashRemaining = initialCash - (schoolFeesPerMonth * paymentsRemaining);

        int initialBankAccount = 100000;
        int accountFeesPerMonth = 20000;
        int feesRemaining = 3;
        int bankAccountRemaining = initialBankAccount - (accountFeesPerMonth * feesRemaining);

        // Création du patrimoine
        var patrimoineZetyAu17sept24 = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au17Sept24,
                Set.of(
                        new Argent("Ordinateur", au17Sept24, valueComputer17Sept2024),
                        new Argent("Vêtements", au17Sept24, valueClothes17Sept2024),
                        new Argent("Espèces", au17Sept24, cashRemaining),
                        new Argent("Compte bancaire", au17Sept24, bankAccountRemaining)));

        // Calcul de la valeur totale attendue
        int expectedTotalValue = valueComputer17Sept2024 + valueClothes17Sept2024 + cashRemaining + bankAccountRemaining;

        // Assertion
        assertEquals(expectedTotalValue, patrimoineZetyAu17sept24.getValeurComptable());
    }

    @Test
    void zety_paendette() {
        var zety = new Personne("Zety");
        var au03Juillet2024 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
        var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

        var espece = new Argent("Espèces", au03Juillet2024, 800_000);
        var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 1);
        var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
        var fraisDeScolarite =
                new FluxArgent(
                        "Frais de scolarité", espece, debutFraisScolarite, finFraisDeScolarite, -200_000, 27);

        var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);

        var fraisDeTenueDeCompte =
                new FluxArgent(
                        "Frais de tenue de compte",
                        compteBancaire,
                        au03Juillet2024,
                        LocalDate.MAX,
                        -20_000,
                        25);

        var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var empruntFraisDeScolarite =
                new FluxArgent(
                        "Emprunt frais de scolarité",
                        compteBancaire,
                        au18Septembre2024,
                        au18Septembre2024,
                        10_000_000,
                        au18Septembre2024.getDayOfMonth());

        // Création de la dette avec la date limite
        var dette = new Dette("Dette emprunt frais de scolarités", au18Septembre2024, -11_000_000, au18Septembre2024);

        var patrimoineZetyAu03Juillet2024 =
                new Patrimoine(
                        "patrimoineZetyAu03Juillet2024",
                        zety,
                        au03Juillet2024,
                        Set.of(ordinateur, vetements, espece, compteBancaire, dette));

        var au17Septembre = LocalDate.of(2024, SEPTEMBER, 17);
        var patrimoineZetyAu17Septembre = patrimoineZetyAu03Juillet2024.projectionFuture(au17Septembre);

        var patrimoineZetyAu18Septembre = patrimoineZetyAu03Juillet2024.projectionFuture(au18Septembre2024);

        var diminutionPatrimoineAttendue = 1_002_384;
        var diminutionPatrimoineActuelle = patrimoineZetyAu17Septembre.getValeurComptable() - patrimoineZetyAu18Septembre.getValeurComptable();

        assertEquals(diminutionPatrimoineAttendue, diminutionPatrimoineActuelle);
    }

    @Test
    void etude_de_zety_2024_2025() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var au18Sept2025 = LocalDate.of(2025, SEPTEMBER, 18);

        // Création des possessions pour l'étude
        var ordinateur = new Materiel("Ordinateur", au3juillet2024, 1_200_000, au3juillet2024, -0.1);
        var vetements = new Materiel("Vêtements", au3juillet2024, 1_500_000, au3juillet2024, -0.5);
        var argentEspece = new Argent("Espèces", au3juillet2024, 800_000);
        var fraisScolarite = new FluxArgent("Frais de scolarité 2023-2024",
                argentEspece,
                au3juillet2024,
                au18Sept2024,
                -200_000,
                27);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);
        var fraisDeTenueDeCompte = new FluxArgent("Frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au18Sept2025,
                -100_000,
                25);
        var detteDansCompte = new FluxArgent("Ajout de dette dans le compte",
                compteBancaire,
                au17Sept2024,
                au18Sept2024,
                10_000_000,
                18);
        var remboursementDedetteDansCompte = new FluxArgent("Remboursement de la dette future dans le compte",
                compteBancaire,
                au18Sept2025.minusDays(1),
                au18Sept2025,
                -11_000_000,
                18);
        var creance = new Creance("Remboursement de la dette", au18Sept2025, 11_000_000);
        var dette = new Dette("Dette auprès d'une banque", au18Sept2024, -11_000_000, au18Sept2024);

        // Création du patrimoine total de Zety
        var patrimoineDeZety = new Patrimoine("Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte, dette, detteDansCompte,
                        remboursementDedetteDansCompte, creance));

        // Calcul de l'évolution du patrimoine jusqu'au 18 septembre 2025
        var patrimoineAuJusquau18Sep = new EvolutionPatrimoine(
                "Étude de Zety 2024-2025",
                patrimoineDeZety,
                au3juillet2024,
                au18Sept2025
        ).getEvolutionJournaliere();

        // Assertions pour vérifier l'évolution attendue du patrimoine
        assertEquals(0, patrimoineAuJusquau18Sep
                .get(LocalDate.of(2025, JANUARY, 1))
                .possessionParNom(argentEspece.getNom()).getValeurComptable());
        assertEquals(250_000, patrimoineAuJusquau18Sep
                .get(LocalDate.of(2024, DECEMBER, 31))
                .possessionParNom(argentEspece.getNom()).getValeurComptable());
    }
}
