package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatrimoineZetyTest {
    @Test
    void ZetyPatrimoineLe17Septembre2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24.minusDays(2),
                -0.10);
        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24.minusDays(2),
                -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                au3juillet24,
                au17septembre24,
                -200_000,
                30);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet24,
                au17septembre24.plusDays(1000),
                -20_000,
                30);

        var patrimoineZety = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

        var valeurFuturePatrimoine = patrimoineZety.projectionFuture(au17septembre24).getValeurComptable();
        // À calculer manuellement ou à partir du code
        long valeurAttendue = 2978848;

        assertEquals(valeurAttendue, valeurFuturePatrimoine);
    }

    @Test
    void diminutionPatrimoineZety() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.50);

        var espece = new Argent("espèce", au3juillet24, 800_000);

        var debutScolarite = LocalDate.of(2023, Month.NOVEMBER, 1);
        var finScolarite = LocalDate.of(2024, Month.AUGUST, 30);
        var fraisDeScolarite = new FluxArgent(
                "Frais de Scolarité",
                espece,
                debutScolarite,
                finScolarite,
                -200_000,
                27);


        var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
        var fraisDuCompte = new FluxArgent(
                "Frais de compte",
                compteBancaire,
                au3juillet24,
                LocalDate.MAX,
                -20_000,
                25);

        var dateEmprunt = LocalDate.of(2024, SEPTEMBER, 18);
        var dateRemb = dateEmprunt.plusYears(1);
        var dette = new Dette("Dette Scolarité", au3juillet24, 0);

        var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
        var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
        var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
        var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

        var detteCompteBancaire = new GroupePossession(
                "Compte Bancaire",
                au3juillet24,
                Set.of(pret, detteAjout, remboursement, detteAnnulation)
        );

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur,
                        vetements,
                        espece,
                        fraisDeScolarite,
                        compteBancaire,
                        fraisDuCompte,
                        dette,
                        detteCompteBancaire
                )
        );

        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);
        assertTrue(patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
        assertEquals(1_002_384, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() - patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
    }

    @Test
    void DepletionEspeceZetyTest() {
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
            if (dateProjection.getDayOfMonth() == 15 && dateProjection.getYear() == 2024) {
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