package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineZetyTest {

    @Test
    void patrimoine_zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var espece = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais tenue de compte",
                compteBancaire, au3juillet24, au17septembre24, -20_000, 25);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                espece, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 30);

        var patrimoineZetyAu17septembre24 = new Patrimoine(
                "patrimoineZetyAu17septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite));

        assertEquals(3_178_848, patrimoineZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable());
    }


    @Test
    void patrimoine_zety_perte_suite_endettement() {
        var zety = new Personne("Zety");

        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au17septembre24, 1_200_000, au17septembre24, -0.0996);
        var vetements = new Materiel("Vêtements", au17septembre24, 1_500_000, au17septembre24, -0.4988);
        var espece = new Argent("Espèces", au17septembre24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais tenue de compte",
                compteBancaire, au17septembre24, au18septembre24, -20_000, 25);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                espece, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 30);
        var endettement = new Dette(
                "Endettement", au18septembre24, -10_000_000);

        var patrimoineZetyAu17septembre24 = new Patrimoine(
                "patrimoineZetyAu17septembre24",
                zety,
                au17septembre24,
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite, endettement));

        long valeurAvantEndettement = patrimoineZetyAu17septembre24.projectionFuture(au18septembre24.minusDays(1)).getValeurComptable();
        long valeurApresEndettement = patrimoineZetyAu17septembre24.projectionFuture(au18septembre24).getValeurComptable();

        long diminutionPatrimoine = valeurAvantEndettement - valeurApresEndettement;
        assertEquals(10_002_378, diminutionPatrimoine);
    }



    @Test
    void zety_n_a_plus_d_especes_apres_february_2025() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, 7, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.0996);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.4988);
        var espece = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais tenue de compte",
                compteBancaire, au3juillet24, LocalDate.of(2024, 9, 25), -20_000, 25);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                espece, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 30);
        var endettement = new Dette(
                "Endettement",
                au3juillet24, -10_000_000);

        var patrimoineZetyAu17septembre24 = new Patrimoine(
                "patrimoineZetyAu17septembre24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, espece, compteBancaire, fraisTenueCompte, fraisScolarite, endettement));

        var paiementScolarite = new FluxArgent(
                "Paiement scolarité 2024-2025",
                compteBancaire, LocalDate.of(2024, 9, 21), LocalDate.of(2024, 9, 21), -2_500_000, 21);

        var donMensuel = new TransfertArgent(
                "Don mensuel des parents",
                new Argent("Espèces", LocalDate.of(2024, 1, 15), 0),
                new Argent("Espèces", LocalDate.of(2024, 1, 15), 0),
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2025, 2, 13),
                100_000,
                15
        );

        var trainDeVie = new FluxArgent(
                "Train de vie mensuel",
                new Argent("Espèces", LocalDate.of(2024, 10, 1), 0), LocalDate.of(2024, 10, 1), LocalDate.of(2025, 2, 13), -250_000, 1);

        var dateLimite = LocalDate.of(2025, 2, 13);
        var valeurComptable = patrimoineZetyAu17septembre24.projectionFuture(dateLimite).getValeurComptable();

        assertEquals(true, patrimoineZetyAu17septembre24.getValeurComptable() < 0, String.valueOf(valeurComptable));
    }
}
