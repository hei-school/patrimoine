package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety() {
        var Zety = new Personne("Zety");
        var au03juillet2024 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("ordinateur", au03juillet2024, 1_200_000, au03juillet2024, -0.10);

        var vetements = new Materiel("vêtements", au03juillet2024, 1_500_000, au03juillet2024, -0.50);

        var argentEspeces = new Argent("argent en espèces", au03juillet2024, 800_000);

        int fraisScolariteTotal = 0;
        LocalDate debutScolarite = LocalDate.of(2024, JULY, 27);
        LocalDate finScolarite = LocalDate.of(2024, AUGUST, 27);
        LocalDate datePaiement = debutScolarite;
        while (!datePaiement.isAfter(finScolarite)) {
            fraisScolariteTotal += 200_000;
            datePaiement = datePaiement.plusMonths(1);
        }
        var fraisScolarite = new Argent("frais de scolarité", au03juillet2024, fraisScolariteTotal);

        int fraisTenueCompte = 0;
        LocalDate debutCompte = LocalDate.of(2024, JULY, 25);
        LocalDate datePonction = debutCompte;
        while (!datePonction.isAfter(LocalDate.of(2024, SEPTEMBER, 17))) {
            fraisTenueCompte += 20_000;
            datePonction = datePonction.plusMonths(1);
        }

        var compteBancaire = new Argent("compte bancaire", au03juillet2024, 100_000 - fraisTenueCompte);

        var au17septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

        var patrimoine_de_zety = new Patrimoine(
                "patrimoineDeZety",
                Zety, au17septembre2024,
                Set.of(
                        ordinateur, vetements, compteBancaire, fraisScolarite
                ));

        assertEquals(2_978_848, patrimoine_de_zety.projectionFuture(au17septembre2024).getValeurComptable());



    }


    @Test
    void patrimoine_de_zety_en_dette() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var patrimoineDeZetyAu17septembre24 = new Patrimoine(
                "patrimoineDeZetyAu17septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        var fluxArgentPret = new FluxArgent(
                "Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
        var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

        var patrimoineDeZetyAu18septembre24 = new Patrimoine(
                "patrimoineDeZetyAu18septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, fluxArgentPret, dette));

        int patrimoinePour17Septembre = patrimoineDeZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable();
        int patrimoinePour18Septembre = patrimoineDeZetyAu18septembre24.projectionFuture(au18septembre24).getValeurComptable();
        int valeurDeDette = patrimoinePour18Septembre - patrimoinePour17Septembre ;

        assertEquals(-1002384, valeurDeDette);
    }

    @Test
    void calculer_date_epuisement_argent_zety() {
        var zety = new Personne("Zety");
        var dateActuelle = LocalDate.of(2024, JULY, 3);
        var dateLimiteFraisScolarite = LocalDate.of(2024, SEPTEMBER, 21);
        var dateLimitePaiementScolarite = LocalDate.of(2024, OCTOBER, 1);
        var dateFinTrainDeVie = LocalDate.of(2025, FEBRUARY, 13);

        var ordinateur = new Materiel("Ordinateur", dateActuelle, 1_200_000, dateActuelle, -0.10);
        var vetements = new Materiel("Vêtements", dateActuelle, 1_500_000, dateActuelle, -0.50);
        var argentEspeces = new Argent("Espèces", dateActuelle, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27), -200_000, 27);

        var compteBancaire = new Argent("Compte bancaire", dateActuelle, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte", compteBancaire, dateActuelle.withDayOfMonth(25),
                LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

        var donParents = new FluxArgent(
                "Don des parents", argentEspeces, LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, DECEMBER, 15), 100_000, 14);

        var trainDeVie = new FluxArgent(
                "Train de vie", argentEspeces, dateLimitePaiementScolarite,
                dateFinTrainDeVie, -250_000, 1);

        var paiementScolarite = new FluxArgent(
                "Paiement scolarité", compteBancaire, dateLimiteFraisScolarite, dateLimiteFraisScolarite, -2_500_000, 21);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                dateActuelle,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

        LocalDate dateEpuisementEspeces = dateActuelle;
        while (patrimoineZetyAu3juillet24.projectionFuture(dateEpuisementEspeces).getValeurComptable() > 0) {
            dateEpuisementEspeces = dateEpuisementEspeces.plusDays(1);
        }

        assertEquals(LocalDate.of(2024, 12, 1), dateEpuisementEspeces);
    }

    @Test
    void testEvaluationPatrimoineZety() {
        var individuZ = new Personne("Zety");
        var dateEvaluation = LocalDate.of(2024, JULY, 3);

        var ordiPortable = new Materiel(
                "Ordinateur portable",
                dateEvaluation,
                1_200_000,
                dateEvaluation.minusDays(1),
                -0.10);
        var habillementZ = new Materiel(
                "Habillement",
                dateEvaluation,
                1_500_000,
                dateEvaluation.minusDays(30),
                -0.50);

        var espèces = new Argent("Espèces", dateEvaluation, 800_000);
        var débutAnnéeScolaire = LocalDate.of(2023, NOVEMBER, 1);
        var finAnnéeScolaire = LocalDate.of(2024, AUGUST, 30);
        var fraisScolarité = new FluxArgent(
                "Frais de scolarité 2023-2024",
                espèces, débutAnnéeScolaire, finAnnéeScolaire, -200_000,
                27);

        var compteBancaire = new Argent("Compte bancaire", dateEvaluation, 100_000);
        var fraisCompte = new FluxArgent(
                "Frais de gestion de compte",
                compteBancaire, dateEvaluation, LocalDate.of(2054, JULY, 1), -20_000,
                25);

        var début2024 = LocalDate.of(2024, JANUARY, 1);
        var donFamille = new FluxArgent(
                "Don familial",
                espèces, début2024, LocalDate.of(2054, JANUARY, 1), 100_000,
                15);

        var datePaiementScolaire = LocalDate.of(2024, SEPTEMBER, 21);
        var fraisScolaritéUnique = new FluxArgent(
                "Frais de scolarité 2024-2025",
                compteBancaire, datePaiementScolaire, datePaiementScolaire, -2_500_000,
                21);

        var débutOctobre2024 = LocalDate.of(2024, OCTOBER, 1);
        var finFévrier2025 = LocalDate.of(2025, FEBRUARY, 13);
        var modeDeVieZ = new FluxArgent(
                "Mode de vie de Zety",
                espèces, débutOctobre2024, finFévrier2025, -250_000,
                1);

        var patrimoineZetyAu3jul2024 = new Patrimoine(
                "Patrimoine de Zety au 3 juillet 2024",
                individuZ,
                dateEvaluation,
                Set.of(new GroupePossession("Le groupe de Zety", dateEvaluation, Set.of(ordiPortable, habillementZ, espèces, compteBancaire))));

        var dateÉvaluationFév2025 = LocalDate.of(2025, FEBRUARY, 1);
        assertTrue(espèces.projectionFuture(dateÉvaluationFév2025).getValeurComptable() < 0);
    }

}


