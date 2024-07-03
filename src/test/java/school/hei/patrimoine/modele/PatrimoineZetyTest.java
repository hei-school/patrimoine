package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.*;
import static java.util.Calendar.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");
        var dateActuelle = LocalDate.of(2024, 7, 3);

        var ordinateur = new Materiel("Ordinateur", dateActuelle, 1_200_000, dateActuelle, -0.10);
        var vetements = new Materiel("Vêtements", dateActuelle, 1_500_000, dateActuelle, -0.50);
        var especes = new Argent("Espèces", dateActuelle, 800_000);

        var dateDebutScolarite = LocalDate.of(2023, 11, 27);
        var dateFinScolarite = LocalDate.of(2024, 8, 27);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes,
                dateDebutScolarite,
                dateFinScolarite,
                -200_000,
                27);

        var compteBancaire = new Argent("Compte bancaire", dateActuelle, 100_000);
        var fraisCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                dateActuelle.minusMonths(1),
                LocalDate.MAX,
                -20_000,
                25);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                dateActuelle,
                Set.of(ordinateur, vetements, especes, fraisScolarite, compteBancaire, fraisCompte));

        var dateFutur = LocalDate.of(2024, 9, 17);
        var patrimoineFuture = patrimoineZety.projectionFuture(dateFutur);

        int valeurTotaleAttendue = 2978848;

        assertEquals(valeurTotaleAttendue, patrimoineFuture.getValeurComptable());
    }

    @Test
    void patrimoine_zety_avec_dette_entre_17_et_18_septembre_2024() {
        var zety = new Personne("Zety");
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au17septembre24, 1_200_000, au17septembre24, -0.10);
        var vetements = new Materiel("Vêtements", au17septembre24, 1_500_000, au17septembre24, -0.50);
        var argentEspeces = new Argent("Espèces", au17septembre24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);

        var fluxArgentPret = new FluxArgent("Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
        var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

        var patrimoineZetyAu17septembre24 = new Patrimoine("patrimoineZetyAu17septembre24", zety, au17septembre24, Set.of(ordinateur, vetements, argentEspeces, compteBancaire));
        var patrimoineZetyAu18septembre24 = new Patrimoine("patrimoineZetyAu18septembre24", zety, au18septembre24, Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fluxArgentPret, dette));

        int valeurPatrimoine17septembre = patrimoineZetyAu17septembre24.getValeurComptable();
        int valeurPatrimoine18septembre = patrimoineZetyAu18septembre24.getValeurComptable();

        int diminutionValeur = valeurPatrimoine18septembre - valeurPatrimoine17septembre;

        assertEquals(-11000000, diminutionValeur);
    }

    @Test
    void date_epuisement_especes_zety() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
        var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
        var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);

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

        var donParents = new FluxArgent(
                "Don des parents", argentEspeces, au3juillet24,
                LocalDate.of(2024, DECEMBER, 15), 100_000, 15);

        var trainDeVie = new FluxArgent(
                "Train de vie", argentEspeces, au1octobre24,
                au13fevrier25, -250_000, 1);

        var paiementScolarite = new FluxArgent(
                "Paiement scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

        if (patrimoineZetyAu3juillet24.projectionFuture(au3juillet24).getValeurComptable() <= 0) {
            assertEquals(au3juillet24, LocalDate.of(2024, OCTOBER, 1));
            return;
        }

        LocalDate dateEpuisementEspeces = au3juillet24;
        while (patrimoineZetyAu3juillet24.projectionFuture(dateEpuisementEspeces).getValeurComptable() > 0) {
            dateEpuisementEspeces = dateEpuisementEspeces.plusDays(1);
        }
        assertEquals(LocalDate.of(2024, DECEMBER, 1), dateEpuisementEspeces);
    }
}
